package com.project.figureout.controller;

import com.project.figureout.ClientNavigator;
import com.project.figureout.dto.*;
import com.project.figureout.model.*;
import com.project.figureout.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/sales")
@SessionAttributes({"salesCardsList", "deliveryAddress", "cartProductTotalPrices"})
public class SaleController {

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    ClientService clientService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private StockService stockService;

    private ClientNavigator clientNavigator;

    @Autowired
    ExchangeService exchangeService;

    @Autowired
    NotificationService notificationService;

    @GetMapping("/makeOrder/{cartId}")
    public String makeOrderGet(@PathVariable long cartId, Model model) {
        // Obtém o carrinho de compras usando o ID fornecido na URL
        Cart cart = cartService.getCartById(cartId);

        // Obtém o cliente associado a este carrinho
        Client client = cartService.getClientByCart(cart);

        // Obtém a lista de endereços e cartões de crédito do cliente
        List<Address> addressClientList = client.getAddresses();
        List<CreditCard> creditCardClientList = client.getCreditCards();

        // Inicializa objetos de transferências de dados para troca de quantidade de produtos no carrinho e cupons
        ChangeCartProductQuantityDTO changeCartProductQuantityDTO = new ChangeCartProductQuantityDTO();
        PromotionalCouponDTO promotionalCouponDTO = new PromotionalCouponDTO();
        ExchangeCouponIndividualDTO exchangeCouponIndividualDTO = new ExchangeCouponIndividualDTO();

        // Cria uma lista de produtos do carrinho e um mapa para armazenar o preço total de cada produto
        List<CartsProducts> cartsProductsList = cart.getCartProducts();
        HashMap<Long, BigDecimal> cartProductTotalPrices = new HashMap<>();

        // Calcula o preço total de cada produto (preço unitário * quantidade)
        for (CartsProducts cartsProducts : cartsProductsList) {
            cartProductTotalPrices.put(cartsProducts.getProduct().getId(), cartsProducts.getFinalPrice());
        }

        // Adiciona os atributos ao modelo para serem utilizados na view "makeOrder"
        model.addAttribute("saleDTO", new SaleDTO());
        model.addAttribute("clientId", client.getId());
        model.addAttribute("cart", cart);
        model.addAttribute("cartProductTotalPrices", cartProductTotalPrices);
        model.addAttribute("addressClientList", addressClientList);
        model.addAttribute("creditCardClientList", creditCardClientList);
        model.addAttribute("changeCartProductQuantityDTO", changeCartProductQuantityDTO);
        model.addAttribute("promotionalCouponDTO", promotionalCouponDTO);
        model.addAttribute("exchangeCouponIndividualDTO", exchangeCouponIndividualDTO);
        model.addAttribute("orderTotalPrice", cart.getTotalPrice());

        // Retorna a view "makeOrder" para ser renderizada
        return "makeOrder";
    }


    @PostMapping("/makeOrder/{cartId}")
    public String makeOrderPost(@PathVariable long cartId, @ModelAttribute SaleDTO saleDTO, Model model, RedirectAttributes redirectAttributes,
                                HttpServletRequest request) {
        // Obtém o carrinho de compras
        Cart cart = cartService.getCartById(cartId);
        System.out.println("address in saleDTO: " + saleDTO.getDeliveryAddressId());

        List<String> errors = new ArrayList<>();

        // Obtém o endereço de entrega usando o ID fornecido no DTO
        try {
            Address deliveryAddress = addressService.getAddressById(saleDTO.getDeliveryAddressId());
            model.addAttribute("deliveryAddress", deliveryAddress);
        } catch (NoSuchElementException exception) {
            errors.add("Nenhum endereço de entrega foi selecionado.");
        }

        if(!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errors", errors);
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        // Inicializa a lista de cartões de crédito usados na compra
        List<SalesCards> salesCardsList = new ArrayList<>();

        // Para cada ID de cartão de crédito no DTO, cria um objeto SalesCards
        for(long creditCardId: saleDTO.getSalesCardsIds()) {
            SalesCards currentSaleCard = new SalesCards();
            currentSaleCard.setCreditCard(creditCardService.getCreditCardById(creditCardId));
            salesCardsList.add(currentSaleCard);
        }

        // Se apenas um cartão de crédito foi usado, define o valor pago como o preço total do carrinho
        if (salesCardsList.size() == 1) {
            salesCardsList.get(0).setAmountPaid(cart.getTotalPrice());
        }

        // Cria um mapa para os preços totais dos produtos no carrinho
        List<CartsProducts> cartsProductsList = cart.getCartProducts();
        HashMap<Long, BigDecimal> cartProductTotalPrices = new HashMap<>();
        for (CartsProducts cartsProducts : cartsProductsList) {
            cartProductTotalPrices.put(cartsProducts.getProduct().getId(), cartsProducts.getFinalPrice());
        }

        // Adiciona os dados ao modelo e redireciona para a página de finalização do pedido
        model.addAttribute("salesCardsList", salesCardsList);

        model.addAttribute("cartProductTotalPrices", cartProductTotalPrices);

        System.out.println("Lista de salescards: " + salesCardsList);

        redirectAttributes.addFlashAttribute("saleCart", cart);
        redirectAttributes.addFlashAttribute("orderTotalPrice", cart.getTotalPrice());

        // Retorna o redirecionamento para a página "finishOrder" com o ID do carrinho
        return "redirect:/sales/finishOrder/" + cartId;
    }

    @GetMapping("/finishOrder/{cartId}")
    public String finishOrderGet(@PathVariable long cartId, Model model) {
        // Obtém o carrinho de compras e o cliente associado
        Cart cart = cartService.getCartById(cartId);
        Client client = cartService.getClientByCart(cart);

        // Obtém o endereço de entrega do modelo (que foi adicionado na requisição anterior)
        Address deliveryAddress = (Address) model.getAttribute("deliveryAddress");
        System.out.println(deliveryAddress.getNickname());


        // Obtém a lista de cartões de crédito usados no pedido (que foi adicionado anteriormente)
        List<SalesCards> listSalesCards = (List<SalesCards>) model.getAttribute("salesCardsList");

        // Inicializa o DTO para os cartões de crédito
        SaleCardDTO saleCardDTO = new SaleCardDTO();
        model.addAttribute("saleCardDTO", saleCardDTO);

        // Calcula o valor do frete com base no estado de entrega
        BigDecimal freight = deliveryAddress.getState().getFreight();
        model.addAttribute("freight", freight);

        // Preenche os valores pagos nos cartões de crédito (que ainda não foram preenchidos)
        for (SalesCards saleCard : listSalesCards) {
            saleCardDTO.getAmountPaid().put(saleCard.getCreditCard().getId(), null);
        }

        // Calcula o valor total do frete
        BigDecimal amountFreight = cart.getTotalPrice().multiply(freight);

        // Calcula o preço final da venda, incluindo o frete
        BigDecimal saleFinalPrice = cart.getTotalPrice().add(amountFreight).setScale(2, RoundingMode.HALF_EVEN);

        System.out.println(saleFinalPrice);

        // Adiciona o preço final ao modelo
        model.addAttribute("saleFinalPrice", saleFinalPrice);
        model.addAttribute("saleCart", cart);

        // Retorna a view "finishOrder"
        return "finishOrder";
    }


    // Define o método para lidar com requisições POST no caminho "/finishOrder/{cartId}"
    @PostMapping("/finishOrder/{cartId}")
    public String createSale(@PathVariable long cartId, @ModelAttribute SaleCardDTO saleCardDTO, Model model,
                             @RequestParam("freight") BigDecimal freight,
                             @RequestParam("saleFinalPrice") BigDecimal saleFinalPrice,
                             RedirectAttributes redirectAttributes) throws IOException {

        // Obtém o carrinho de compras (Cart) com base no ID fornecido na URL
        Cart cart = cartService.getCartById(cartId);

        // Cria uma nova instância de Sale (Venda)
        Sale sale = new Sale();

        // Define os intervalos de caracteres permitidos para gerar o código da venda (letras e números)
        char[][] allowedCharacterRanges = {{'a','z'},{'A','Z'},{'0','9'}};

        // Usando o RandomStringGenerator para gerar um código aleatório de 6 caracteres para a venda
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(allowedCharacterRanges)
                .build();

        // Gera o código da venda, prefixando com "#"
        String saleCode = "#" + generator.generate(6);

        // Define o código da venda no objeto Sale
        sale.setSaleCode(saleCode);

        // Associa o carrinho de compras à venda
        sale.setCart(cart);

        // Obtém o endereço de entrega do modelo (provavelmente setado anteriormente na página)
        Address deliveryAddress = (Address) model.getAttribute("deliveryAddress");

        sale.setDeliveryAddress(deliveryAddress);

        // Obtém a lista de cartões de crédito usados no pagamento da venda
        List<SalesCards> listSalesCards = (List<SalesCards>) model.getAttribute("salesCardsList");

        // Define o frete e o cupom promocional da venda
        sale.setFreight(freight);
        sale.setPromotionalCouponApplied(sale.getCart().getPromotionalCoupon());

        // Define o preço final da venda
        sale.setFinalPrice(saleFinalPrice);

        // Inicializa a variável para o total pago pelos cartões de crédito
        BigDecimal totalPaidByCards = BigDecimal.valueOf(0);

        // Lista para armazenar erros encontrados durante a validação
        List<String> errors = new ArrayList<>();

        // Percorre os valores pagos por cada cartão de crédito e atualiza os valores dos cartões na venda
        for (Map.Entry<Long, BigDecimal> entry : saleCardDTO.getAmountPaid().entrySet()) {
            Long key = entry.getKey();
            BigDecimal value = entry.getValue();

            if(listSalesCards.isEmpty()) {
                errors.add("Você não selecionou nenhum cartão.");
            }

            // Para cada cartão de crédito na lista de cartões, verifica se o ID corresponde ao cartão atual
            for (SalesCards saleCard : listSalesCards) {
                if (saleCard.getCreditCard().getId() == key) {

                    // Atualiza o valor pago por esse cartão
                    saleCard.setAmountPaid(value);

                    CreditCard specificCreditCard = saleCard.getCreditCard();
                    BigDecimal creditCardBalance = specificCreditCard.getBalance();

                    if(creditCardBalance.compareTo(saleCard.getAmountPaid()) < 0) {
                        errors.add("O cartão " + specificCreditCard.getNickname() + " não tem saldo suficiente para pagar pela compra.");
                    }

                    // Acumula o total pago pelos cartões
                    totalPaidByCards = totalPaidByCards.add(saleCard.getAmountPaid());
                }
            }


        }

        if(!cart.getExchangeCoupons().isEmpty()) {
            List<ExchangeCoupon> listOfExchangeCoupons = cart.getExchangeCoupons();

            for(ExchangeCoupon exchangeCoupon : listOfExchangeCoupons) {
                sale.getExchangeCouponsApplied().add(exchangeCoupon);
            }

        }

        // Verifica se não há cupom promocional e se não há cupons de troca aplicados
        if (sale.getPromotionalCouponApplied() == null && sale.getExchangeCouponsApplied().isEmpty()) {

            // Verifica se o valor pago por cada cartão é maior ou igual a R$10,00
            for (SalesCards saleCard : listSalesCards) {
                if (saleCard.getAmountPaid().compareTo(BigDecimal.valueOf(10.00)) < 0) {
                    errors.add("O valor pago pelo cartão " + saleCard.getCreditCard().getCardNumber() + " não pode ser inferior a R$10,00.");
                }
            }
        }

        if(sale.getPromotionalCouponApplied() != null) {

            if(sale.getPromotionalCouponApplied().getCouponExpirationDate().isBefore(LocalDate.now())) {
                errors.add("O cupom promocional " + sale.getPromotionalCouponApplied().getCouponName() + " expirou. Remova-o da compra.");
            }

            List<Sale> listOfClientSales = saleService.getClientSalesByClientId(sale.getCart().getClient().getId());

            for(Sale currentSale : listOfClientSales) {
                if(currentSale.getPromotionalCouponApplied() != null) {
                    if(currentSale.getPromotionalCouponApplied().getId() == sale.getPromotionalCouponApplied().getId()) {
                        errors.add("O cupom promocional " + sale.getPromotionalCouponApplied().getCouponName() + " já foi utilizado. Remova-o da compra.");
                    }
                }
            }

        }

        // Log dos preços finais para depuração
        System.out.println("Preço final da venda: " + saleFinalPrice);
        System.out.println("Total pago pelos cartões: " + totalPaidByCards);

        // Verifica se o total pago pelos cartões é igual ao preço final da venda
        if (totalPaidByCards.compareTo(saleFinalPrice) != 0) {
            errors.add("O total pago pelos cartões é excedente ou insuficiente para pagar pela compra.");
        }

        // Obtém o preço total dos produtos no carrinho (presumivelmente calculado anteriormente)
        HashMap<Long, BigDecimal> cartProductTotalPrice = (HashMap<Long, BigDecimal>) model.getAttribute("cartProductTotalPrice");

        if(sale.getCart().getCartProducts().isEmpty()) {
            errors.add("Você não pode fazer uma compra com nenhum produto.");
        }

        if(!sale.getCart().getClient().isEnabled()) {
            errors.add("Você não pode fazer compras com uma conta inativada.");
        }

        for(CartsProducts currentCartProduct: sale.getCart().getCartProducts()) {
            Stock stock = stockService.getProductInStockByProductId(currentCartProduct.getProduct().getId());

            if(currentCartProduct.getProductQuantity() > stock.getProductQuantityAvailable()) {
                errors.add("O produto " + currentCartProduct.getProduct().getName() + " só tem " + stock.getProductQuantityAvailable() + " unidades disponíveis para compra.");
            }

            if(!currentCartProduct.getProduct().isActive()) {
                errors.add("O produto " + currentCartProduct.getProduct().getName() + " está atualmente inativo para compras.");
            }

        }

        // Se houver erros, adiciona os dados ao modelo e retorna para a página de "finalizar pedido"
        if (!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("saleCart", cart);

            return "redirect:/sales/finishOrder/" + cartId;
        }

        // Salva a venda no banco de dados
        saleService.saveSale(sale);

        // Atualiza as informações dos cartões de crédito usados na venda
        for (Map.Entry<Long, BigDecimal> entry : saleCardDTO.getAmountPaid().entrySet()) {
            Long key = entry.getKey();

            for (SalesCards saleCard : listSalesCards) {
                if (saleCard.getCreditCard().getId() == key) {
                    // Cria a chave composta para o relacionamento entre a venda e o cartão de crédito
                    SalesCardsKey salesCardsKey = new SalesCardsKey();
                    salesCardsKey.setCreditCardId(creditCardService.getCreditCardById(key).getId());
                    salesCardsKey.setSaleId(sale.getId());

                    // Define o ID e a venda para o cartão de crédito
                    saleCard.setId(salesCardsKey);
                    saleCard.setSale(sale);

                    // Adiciona o cartão usado na venda à lista de cartões da venda
                    sale.getCardsUsedInThisSale().add(saleCard);
                }
            }
        }

        // Verifica se o valor do cupom de troca é superior ao valor do carrinho e gera novos cupons de troca se necessário
        for (ExchangeCoupon exchangeCoupon : sale.getExchangeCouponsApplied()) {
            if (cartService.isExchangeCouponSurpassingCartTotalTooMuch(cart, exchangeCoupon).compareTo(BigDecimal.valueOf(0)) > 0) {
                exchangeService.generateExchangeCouponSurpass(cart.getClient(), cartService.isExchangeCouponSurpassingCartTotalTooMuch(cart, exchangeCoupon));
            }
        }

        // Define o status da venda como "Em processamento"
        sale.setStatus(SaleStatusEnum.EM_PROCESSAMENTO);

        // Define a data e hora da venda
        LocalDateTime now = LocalDateTime.now();
        sale.setDateTimeSale(now);

        // Obtém o cliente associado à venda
        Client client = clientService.getClientById(clientNavigator.getInstance().getClientId());

        // Salva a venda no banco de dados novamente (isso pode ser redundante)
        saleService.saveSale(sale);

        // Atualiza o carrinho do cliente após a venda
        cartService.changeClientCart(client);

        // Cria e envia uma notificação para o cliente informando que a compra foi realizada com sucesso
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setCategory(NotificationCategoryEnum.VENDA);
        notificationDTO.setTitle("Compra realizada!");
        notificationDTO.setDescription("Sua compra de R$" + sale.getFinalPrice() + " foi realizada com sucesso.");
        notificationService.createNotification(client, notificationDTO);

        HashMap<Stock, Integer> cartProductQuantityToRemove = new HashMap<>();

        for(CartsProducts currentCartProduct: sale.getCart().getCartProducts()) {
            Stock stock = stockService.getProductInStockByProductId(currentCartProduct.getProduct().getId());
            cartProductQuantityToRemove.put(stock, currentCartProduct.getProductQuantity());

            if(currentCartProduct.getProductQuantity() >= stock.getProductQuantityAvailable()) {
                productService.inactivateProduct(stock.getProduct());
            }

        }

        stockService.dropInStockList(cartProductQuantityToRemove);

        // Recalcula o ranking do cliente após a compra
        client.setTotalAmountSpent(client.getTotalAmountSpent().add(saleFinalPrice));
        clientService.saveClient(client);
        //clientService.recalculateClientRanking();

        for (SalesCards saleCard : listSalesCards) {
            CreditCard creditCard = saleCard.getCreditCard();

            creditCard.setBalance(creditCard.getBalance().subtract(saleCard.getAmountPaid()));

            creditCardService.saveCreditCard(creditCard);
        }

        ChangeSaleStatusDTO changeSaleStatusDTO = new ChangeSaleStatusDTO();
        changeSaleStatusDTO.setStatus(SaleStatusEnum.PAGAMENTO_REALIZADO);
        saleService.changeSaleStatus(sale, changeSaleStatusDTO);

        for (ExchangeCoupon exchangeCoupon : sale.getExchangeCouponsApplied()) {
            exchangeCoupon.setUsed(true);
            exchangeService.getExchangeCouponRepository().save(exchangeCoupon);
        }

        // Redireciona o usuário para a página de compras após concluir o pedido
        return "redirect:/products/shop";
    }


    // Define o método para lidar com requisições GET no caminho "/seeSales"
    @GetMapping("/seeSales")
    public String seeSales(Model model) {
        // Cria uma nova instância de ChangeSaleStatusDTO e a adiciona ao modelo
        model.addAttribute("changeSaleStatusDTO", new ChangeSaleStatusDTO());
        model.addAttribute("changeExchangeStatusDTO", new ChangeExchangeStatusDTO());

        // Cria uma nova instância de ChangeSaleDateTimeDTO e a adiciona ao modelo
        model.addAttribute("changeSaleDateTimeDTO", new ChangeSaleDateTimeDTO());

        // Adiciona todos os valores possíveis do enum SaleStatusEnum ao modelo para seleção no front-end
        model.addAttribute("status", SaleStatusEnum.values());
        model.addAttribute("exchangeStatus", ExchangeStatusEnum.values());

        // Adiciona a lista de todas as vendas ao modelo, obtida do serviço saleService
        model.addAttribute("sales", saleService.getAllSales());

        // Adiciona a lista de todas as trocas ao modelo, obtida do serviço exchangeService
        model.addAttribute("exchanges", exchangeService.getAllExchanges());

        // Retorna o nome da view (HTML/JSP) que será renderizada, neste caso "adminSalesView"
        return "adminSalesView";
    }


    // Define o método para lidar com requisições PUT no caminho "/seeSales/changeSaleStatus/{saleId}"
    @PutMapping("/seeSales/changeSaleStatus/{saleId}")
    public String changeSaleStatus(@PathVariable long saleId, @ModelAttribute ChangeSaleStatusDTO changeSaleStatusDTO) throws IOException {
        // Chama o serviço saleService para alterar o status da venda, passando a venda encontrada pelo ID e os dados do DTO
        saleService.changeSaleStatus(saleService.getSaleById(saleId), changeSaleStatusDTO);

        // Após alterar o status da venda, redireciona o usuário para a página "/sales/seeSales"
        return "redirect:/sales/seeSales";
    }


    // Define o método para lidar com requisições GET no caminho "/getSaleExchangeList/{saleId}"
    @GetMapping("/getSaleExchangeList/{saleId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public List<Exchange> getSaleExchangeList(@PathVariable long saleId) {
        // Obtém a venda correspondente ao ID fornecido (saleId) utilizando o serviço saleService
        Sale sale = saleService.getSaleById(saleId);

        // Retorna a lista de trocas associada à venda
        return sale.getExchangeList();
    }

    // Define o método para lidar com requisições GET no caminho "/getSpecificExchange/{exchangeId}"
    @GetMapping("/getSpecificExchange/{exchangeId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public Exchange getSpecificExchange(@PathVariable long exchangeId) {
        // Obtém a troca específica utilizando o serviço exchangeService
        Exchange exchange = exchangeService.getExchangeById(exchangeId);

        // Retorna a troca específica
        return exchange;
    }

    // Define o método para lidar com requisições GET no caminho "/getSaleCartId/{saleId}"
    @GetMapping("/getSaleCartId/{saleId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public long getSaleCartId(@PathVariable long saleId, Model model) {
        // Obtém a venda correspondente ao ID fornecido (saleId) utilizando o serviço saleService
        Sale sale = saleService.getSaleById(saleId);

        // Retorna o ID do carrinho associado à venda
        return sale.getCart().getId();
    }

    // Define o método para lidar com requisições GET no caminho "/getSaleDate/{saleId}"
    @GetMapping("/getSaleDate/{saleId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public LocalDateTime getSaleDate(@PathVariable long saleId, Model model) {
        // Obtém a venda correspondente ao ID fornecido (saleId) utilizando o serviço saleService
        Sale sale = saleService.getSaleById(saleId);

        // Retorna a data e hora da venda
        return sale.getDateTimeSale();
    }

    // Define o método para lidar com requisições GET no caminho "/getSaleCode/{saleId}"
    @GetMapping("/getSaleCode/{saleId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public String getSaleCode(@PathVariable long saleId, Model model) {
        // Obtém a venda correspondente ao ID fornecido (saleId) utilizando o serviço saleService
        Sale sale = saleService.getSaleById(saleId);

        // Retorna o código da venda
        return sale.getSaleCode();
    }

    // Define o método para lidar com requisições GET no caminho "/getSaleCartProducts/{saleId}"
    @GetMapping("/getSaleCartProducts/{saleId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public List<CartsProducts> getSaleCartProducts(@PathVariable long saleId, Model model) {
        // Obtém a venda correspondente ao ID fornecido (saleId) utilizando o serviço saleService
        Sale sale = saleService.getSaleById(saleId);

        // Retorna a lista de produtos do carrinho associado à venda
        return sale.getCart().getCartProducts();
    }

    // Define o método para lidar com requisições GET no caminho "/getSaleClientId/{saleId}"
    @GetMapping("/getSaleClientId/{saleId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public long getSaleClientId(@PathVariable long saleId, Model model) {
        // Obtém a venda correspondente ao ID fornecido (saleId) utilizando o serviço saleService
        Sale sale = saleService.getSaleById(saleId);

        // Retorna o ID do cliente associado ao carrinho da venda
        return sale.getCart().getClient().getId();
    }

    // Define o método para lidar com requisições GET no caminho "/getSaleClientName/{saleId}"
    @GetMapping("/getSaleClientName/{saleId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public String getSaleClientName(@PathVariable long saleId, Model model) {
        // Obtém a venda correspondente ao ID fornecido (saleId) utilizando o serviço saleService
        Sale sale = saleService.getSaleById(saleId);

        // Retorna o nome do cliente associado ao carrinho da venda
        return sale.getCart().getClient().getName();
    }


    // Define o método para lidar com requisições GET no caminho "/getSaleTotal/{saleId}"
    @GetMapping("/getSaleTotal/{saleId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public BigDecimal getSaleTotal(@PathVariable long saleId, Model model) {
        // Obtém a venda correspondente ao ID fornecido (saleId) utilizando o serviço saleService
        Sale sale = saleService.getSaleById(saleId);

        // Imprime o valor do preço final da venda no console (apenas para debug)
        System.out.println(sale.getFinalPrice());

        // Retorna o valor do preço final da venda
        return sale.getFinalPrice();
    }


    // Define o método para lidar com requisições GET no caminho "/getFreight/{saleId}"
    @GetMapping("/getFreight/{saleId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public BigDecimal getFreight(@PathVariable long saleId, Model model) {
        // Obtém a venda correspondente ao ID fornecido (saleId) utilizando o serviço saleService
        Sale sale = saleService.getSaleById(saleId);

        // Retorna o valor do frete associado à venda
        return sale.getFreight();
    }

    // Define o método para lidar com requisições GET no caminho "/getPromotionalCoupon/{saleId}"
    @GetMapping("/getPromotionalCoupon/{saleId}")
    @ResponseBody // A anotação @ResponseBody indica que o retorno do método será diretamente convertido para o corpo da resposta HTTP
    public ResponseEntity<?> getPromotionalCoupon(@PathVariable long saleId, Model model) {
        // Obtém a venda correspondente ao ID fornecido (saleId) utilizando o serviço saleService
        Sale sale = saleService.getSaleById(saleId);

        // Obtém o cupom promocional associado ao carrinho da venda
        PromotionalCoupon coupon = sale.getCart().getPromotionalCoupon();

        // Verifica se o cupom é nulo
        if (coupon == null) {
            // Se não houver cupom, retorna uma resposta HTTP 200 OK com um mapa vazio
            return ResponseEntity.ok().body(Collections.emptyMap());
        }

        // Se o cupom não for nulo, retorna a resposta HTTP 200 OK com o cupom
        return ResponseEntity.ok(coupon);
    }

    // Define o método para lidar com requisições HTTP PUT no caminho "/seeSales/changeSaleDateTime/{saleId}"
    /*
    @PutMapping("/seeSales/changeSaleDateTime/{saleId}")
    public String changeSaleDateTime(@PathVariable long saleId, @ModelAttribute ChangeSaleDateTimeDTO changeSaleDateTimeDTO) {
        // Obtém a venda com o ID fornecido (saleId) usando o serviço saleService.
        Sale sale = saleService.getSaleById(saleId);

        // Altera a data e hora da venda com base no novo valor fornecido pelo DTO
        sale.setDateTimeSale(changeSaleDateTimeDTO.getNewDateTime());

        // Salva a venda atualizada de volta no banco de dados usando o serviço saleService
        saleService.saveSale(sale);

        // Redireciona o usuário para a página "/sales/seeSales" após a alteração
        return "redirect:/sales/seeSales";
    }*/




}
