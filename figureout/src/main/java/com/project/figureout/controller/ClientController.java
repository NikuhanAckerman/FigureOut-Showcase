package com.project.figureout.controller;

import com.project.figureout.ClientNavigator;
import com.project.figureout.dto.*;
import com.project.figureout.model.*;
import com.project.figureout.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private StateAndCountryService stateAndCountryService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private NotificationService notificationService;

    private ClientNavigator clientNavigator;

    @GetMapping("/showAllClients")
    public String showClientsGet(Model model) {
        List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);

        return "showClients";
    }

    @GetMapping("index/{id}/addresses") // exemplo de paranaue com javascript (o codigo js ta na pagina index)
    @ResponseBody
    public List<Address> getClientAddresses(@PathVariable long id) {
        return clientService.getClientAddresses(id);
    }

    @GetMapping("/createAddress/{id}")
    public String addClientGet(Model model, @PathVariable long id, @RequestParam(defaultValue = "/showAllClients") String redirectToInPost) {
        AddressDTO addressDTO = new AddressDTO();

        List<State> stateList = stateAndCountryService.getAllStates();
        List<Country> countryList = stateAndCountryService.getAllCountries();

        model.addAttribute("stateList", stateList);
        model.addAttribute("countryList", countryList);
        model.addAttribute("addressDTO", addressDTO);
        model.addAttribute("clientId", id);
        model.addAttribute("redirectToInPost", redirectToInPost);

        return "createAddress";
    }

    @PostMapping("createAddress/{id}")
    public String createClientAddressPost(@PathVariable long id, @Valid @ModelAttribute("addressDTO") AddressDTO addressDTO, BindingResult result, Model model,
                                          @RequestParam(defaultValue = "/showAllClients") String redirectToInPost) {
        Client client = clientService.getClientById(id);

        if(result.hasErrors()) {
            List<State> stateList = stateAndCountryService.getAllStates();
            List<Country> countryList = stateAndCountryService.getAllCountries();
            model.addAttribute("stateList", stateList);
            model.addAttribute("countryList", countryList);
            model.addAttribute("clientId", id);
            model.addAttribute("redirectToInPost", redirectToInPost);

            return "createAddress";
        }

        addressService.registerAddress(client, addressDTO);

        return "redirect:" + redirectToInPost;
    }

    @DeleteMapping("/deleteAddress/{id}")
    public String deleteClientAddress(@PathVariable long id) {
        addressService.deleteAddress(id);

        return "redirect:/showAllClients";
    }

    @GetMapping("updateAddress/{addressId}")
    public String updateClientAddressGet(@PathVariable long addressId, Model model,
                                         @RequestParam(defaultValue = "/showAllClients") String redirectToInPut) {
        Address address = addressService.getAddressById(addressId);
        AddressDTO addressDTO = new AddressDTO();

        System.out.println(redirectToInPut);

        List<State> stateList = stateAndCountryService.getAllStates();
        List<Country> countryList = stateAndCountryService.getAllCountries();

        addressService.populateAddressDTO(addressDTO, address);

        model.addAttribute("addressDTO", addressDTO);
        model.addAttribute("stateList", stateList);
        model.addAttribute("countryList", countryList);
        model.addAttribute("redirectToInPut", redirectToInPut);

        return "updateAddress";
    }

    @PutMapping("updateAddress/{addressId}")
    public String updateClientAddressPost(@PathVariable long addressId, @Valid @ModelAttribute("addressDTO") AddressDTO addressDTO, BindingResult result, Model model,
                                          @RequestParam(defaultValue = "/showAllClients") String redirectToInPut) {
        Address addressToUpdate = addressService.getAddressById(addressId);
        List<State> stateList = stateAndCountryService.getAllStates();
        List<Country> countryList = stateAndCountryService.getAllCountries();

        if(result.hasErrors()) {
            model.addAttribute("addressId", addressId);
            model.addAttribute("stateList", stateList);
            model.addAttribute("countryList", countryList);
            model.addAttribute("redirectToInPut", redirectToInPut);

            return "updateAddress";
        }

        addressService.updateAddress(addressId, addressDTO);

        return "redirect:" + redirectToInPut;
    }

    @GetMapping("index/{id}/creditCards")
    @ResponseBody
    public List<CreditCard> getClientCreditCards(@PathVariable long id) {
        return creditCardService.getClientCreditCards(id);
    }

    @GetMapping("createCreditCard/{id}")
    public String createClientCreditCardGet(@PathVariable long id, Model model, @RequestParam(defaultValue = "/showAllClients") String redirectToInPost) {
        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setClientId(id);

        List<CreditCardBrand> creditCardBrandList = creditCardService.getAllCreditCardBrands();

        model.addAttribute("creditCardDTO", creditCardDTO);
        model.addAttribute("clientId", id);
        model.addAttribute("creditCardBrandList", creditCardBrandList);
        model.addAttribute("redirectToInPost", redirectToInPost);

        return "createCreditCard";
    }

    @PostMapping("createCreditCard/{id}")
    public String createClientCreditCardPost(@PathVariable long id, @Valid @ModelAttribute CreditCardDTO creditCardDTO, BindingResult result, Model model,
                                             @RequestParam(defaultValue = "/showAllClients") String redirectToInPost) {
        Client client = clientService.getClientById(id);
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";

        if(result.hasErrors()) {
            List<CreditCardBrand> creditCardBrandList = creditCardService.getAllCreditCardBrands();

            System.out.println(ANSI_RED + "there are errors in credit card creation" + ANSI_RESET);

            model.addAttribute("creditCardDTO", creditCardDTO);
            model.addAttribute("clientId", id);
            model.addAttribute("creditCardBrandList", creditCardBrandList);
            model.addAttribute("redirectToInPost", redirectToInPost);

            return "createCreditCard";
        }

        System.out.println(ANSI_RED + "no errors in credit card creation" + ANSI_RESET);

        creditCardService.registerCreditCard(client, creditCardDTO);

        return "redirect:" + redirectToInPost;

    }

    @DeleteMapping("index/{id}/creditCards/delete")
    public String deleteClientCreditCard(@PathVariable long id) {
        creditCardService.deleteCreditCard(id);

        return "redirect:/showAllClients";
    }

    @GetMapping("updateCreditCard/{id}")
    public String updateClientCreditCardGet(@PathVariable long id, Model model,
                                            @RequestParam(defaultValue = "/showAllClients") String redirectToInPut) {
        CreditCard creditCard = creditCardService.getCreditCardById(id);
        Client client = creditCard.getClient();

        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setClientId(client.getId());
        creditCardDTO.setCreditCardId(creditCard.getId());

        List<CreditCardBrand> creditCardBrandList = creditCardService.getAllCreditCardBrands();

        creditCardService.populateCreditCardDTO(creditCardDTO, creditCard);

        model.addAttribute("creditCardDTO", creditCardDTO);
        model.addAttribute("creditCardBrandList", creditCardBrandList);
        model.addAttribute("creditCardId", id);
        model.addAttribute("redirectToInPut", redirectToInPut);

        return "updateCreditCard";
    }

    @PutMapping("updateCreditCard/{id}")
    public String updateClientCreditCardPut(@PathVariable long id, @Valid @ModelAttribute CreditCardDTO creditCardDTO, BindingResult result, Model model,
                                            @RequestParam(defaultValue = "/showAllClients") String redirectToInPut) {
        CreditCard creditCardToUpdate = creditCardService.getCreditCardById(id);
        List<CreditCardBrand> creditCardBrandList = creditCardService.getAllCreditCardBrands();

        if(result.hasErrors()) {
            model.addAttribute("creditCardId", id);
            model.addAttribute("creditCardBrandList", creditCardBrandList);
            model.addAttribute("redirectToInPut", redirectToInPut);

            return "updateCreditCard";
        }

        creditCardService.updateCreditCard(creditCardToUpdate, creditCardDTO);

        return "redirect:" + redirectToInPut;
    }

    @GetMapping("/createClient")
    public String addClientGet(Model model) {
        ClientDTO clientDTO = new ClientDTO();
        List<Gender> genderList = clientService.getAllGenders();
        List<State> stateList = stateAndCountryService.getAllStates();
        List<Country> countryList = stateAndCountryService.getAllCountries();

        model.addAttribute("clientDTO", clientDTO);
        model.addAttribute("genderList", genderList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("countryList", countryList);

        return "createClient";
    }

    @PostMapping("/createClient")
    public String addClientPost(@Valid @ModelAttribute("clientDTO") ClientDTO clientDTO, BindingResult result, Model model) throws Exception {

        Client client = new Client();

        if(result.hasErrors()) {
            List<Gender> genderList = clientService.getAllGenders();
            List<State> stateList = stateAndCountryService.getAllStates();
            List<Country> countryList = stateAndCountryService.getAllCountries();
            model.addAttribute("genderList", genderList);
            model.addAttribute("stateList", stateList);
            model.addAttribute("countryList", countryList);
            model.addAttribute("clientId", client.getId());

            return "createClient";
        }

        clientService.registerClient(client, clientDTO);

        return "redirect:/showAllClients";
    }

    // Método no controller pra página de trocar de senha.
    @GetMapping("/changePassword/{clientId}")
    public String showChangePasswordForm(@ModelAttribute ClientChangePasswordDTO changePasswordDTO,
                                         @PathVariable Long clientId,
                                         Model model,
                                         @RequestParam(defaultValue = "/showAllClients") String redirectToInPost) {
        model.addAttribute("changePasswordDTO", new ClientChangePasswordDTO());
        model.addAttribute("clientId", clientId);
        model.addAttribute("redirectToInPost", redirectToInPost);

        return "changePassword"; // Template do thymeleaf
    }

    @PostMapping("/changePassword/{clientId}")
    public String changePassword(@Valid @ModelAttribute("changePasswordDTO") ClientChangePasswordDTO changePasswordDTO, BindingResult result,
                                 @PathVariable Long clientId,
                                 Model model,
                                 @RequestParam(defaultValue = "/showAllClients") String redirectToInPost) throws Exception {
        // Chamar o serviço para mudar a senha.
        if(result.hasErrors()) {
            model.addAttribute("redirectToInPost", redirectToInPost);
            return "changePassword";
        }

        clientService.changePassword(clientId, changePasswordDTO);

        return "redirect:" + redirectToInPost;
    }

    @GetMapping("/updateClient/{id}")
    public String updateClientGet(@PathVariable long id, Model model) throws Exception {

        Client client = clientService.getClientById(id);

        ClientBasicDataDTO clientBasicDataDTO = new ClientBasicDataDTO();
        System.out.println(id);
        clientBasicDataDTO.setClientId(id);
        System.out.println(clientBasicDataDTO.getClientId());

        clientService.populateClientBasicDataDTO(clientBasicDataDTO, client);

        List<Gender> genderList = clientService.getAllGenders();
        model.addAttribute("clientBasicDataDTO", clientBasicDataDTO);
        model.addAttribute("genderList", genderList);
        model.addAttribute("clientId", id);

        return "updateClient";

    }

    @PutMapping("/updateClient/{id}")
    public String updateClient(@PathVariable long id, @Valid @ModelAttribute ClientBasicDataDTO clientBasicDataDTO, BindingResult result, Model model) throws Exception {

        Client clientToUpdate = clientService.getClientById(id);

        if(result.hasErrors()) {
            List<Gender> genderList = clientService.getAllGenders();
            model.addAttribute("clientId", id);
            model.addAttribute("genderList", genderList);

            return "updateClient";
        }

        clientService.updateClient(clientToUpdate, clientBasicDataDTO);


        return "redirect:/showAllClients";

    }

    @DeleteMapping("/deleteClient/{id}")
    public String deleteClient(@PathVariable long id) {
        clientService.deleteClientById(id);
        //clientService.recalculateClientRanking();

        return "redirect:/showAllClients";
    }


    // Filtro de clientes no CRUD de clientes
    @GetMapping("/clients")
    public String getClients(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String password,
                             @RequestParam(required = false) String cpf,
                             @RequestParam(required = false) LocalDate birthday,
                             @RequestParam(required = false) String phone,
                             @RequestParam(required = false) String active,
                             @RequestParam(required = false) String gender,
                             @RequestParam(required = false) Long id,
                             Model model) throws Exception {
        List<Client> clients = clientService.filterClients(name, email, password, cpf, birthday, phone, active, gender, id);
        model.addAttribute("clients", clients);
        model.addAttribute("filterName", name);
        model.addAttribute("filterEmail", email);
        model.addAttribute("filterPassword", password);
        model.addAttribute("filterCpf", cpf);
        model.addAttribute("filterBirthday", birthday);
        model.addAttribute("filterPhone", phone);
        model.addAttribute("filterActive", active);
        model.addAttribute("filterGender", gender);
        model.addAttribute("filterId", id);
        return "showClients";
    }

    @GetMapping("/clientProfileGeneral/{id}")
    public String seeClientProfile(@PathVariable long id, Model model) throws Exception {
        Client client = clientService.getClientById(id);

        int notificationQuantity = notificationService.getClientNotifications(client.getId()).size();

        model.addAttribute("id", id);
        model.addAttribute("name", client.getName());
        model.addAttribute("email", client.getEmail());
        model.addAttribute("password", client.getPassword());
        model.addAttribute("gender", client.getGender());
        model.addAttribute("cpf", client.getCpf());
        model.addAttribute("phone", client.getPhone());
        model.addAttribute("notificationQuantity", notificationQuantity);

        return "clientProfile";
    }

    @GetMapping("/clientProfileNotifications/{id}")
    public String seeClientProfileNotifications(@PathVariable long id, Model model) {
        Client client = clientService.getClientById(id);
        List<Notification> clientNotifications = notificationService.getClientNotifications(client.getId());
        int notificationQuantity = notificationService.getClientNotifications(client.getId()).size();

        model.addAttribute("notifications", clientNotifications);
        model.addAttribute("notificationQuantity", notificationQuantity);

        return "clientProfileNotifications";
    }

    @DeleteMapping("/clientRemoveNotification/{id}")
    public String clientRemoveNotification(@PathVariable long id, Model model) {
        Client client = notificationService.getClientByNotification(notificationService.getNotificationById(id));
        notificationService.deleteNotificationById(id);

        return "redirect:/clientProfileNotifications/" + client.getId();
    }

    @GetMapping("/clientProfilePurchases/{id}")
    public String seeClientProfilePurchases(@PathVariable long id, Model model) {
        List<Sale> clientSales = (List<Sale>) model.asMap().get("sales");

        if (clientSales == null) {
            clientSales = saleService.getClientSalesByClientId(id);
        }

        int notificationQuantity = notificationService.getClientNotifications(id).size();

        model.addAttribute("id", id);
        model.addAttribute("sales", clientSales);
        model.addAttribute("notificationQuantity", notificationQuantity);

        model.addAttribute("saleStatus", SaleStatusEnum.values());
        model.addAttribute("entregueStatus", SaleStatusEnum.ENTREGUE);
        model.addAttribute("trocaFinalizadaStatus", SaleStatusEnum.TROCA_FINALIZADA);
        model.addAttribute("trocaNaoAutorizadaStatus", SaleStatusEnum.TROCA_NAO_AUTORIZADA);

        HashMap<CartsProductsKey, List<ExchangeShowOnPurchasesDTO>> productIdExchangeInfo = new HashMap<>();

        for(Sale currentSale: clientSales) {

            for(Exchange currentExchange: currentSale.getExchangeList()) {

                for(ExchangeProducts currentExchangeProduct: currentExchange.getReturnedProducts()) {
                    System.out.println("ID do carrinho do exchange product: " + currentExchangeProduct.getCartProduct().getId().getCartId());

                    ExchangeShowOnPurchasesDTO exchangeShowOnPurchasesDTO = new ExchangeShowOnPurchasesDTO();

                    exchangeShowOnPurchasesDTO.setExchangeCode(currentExchange.getExchangeCode());
                    exchangeShowOnPurchasesDTO.setStatus(currentExchange.getStatus());
                    exchangeShowOnPurchasesDTO.setCartsProductsKey(currentExchangeProduct.getCartProduct().getId());
                    exchangeShowOnPurchasesDTO.setQuantityReturned(currentExchangeProduct.getQuantityReturned());
                    ;
                    productIdExchangeInfo.computeIfAbsent(exchangeShowOnPurchasesDTO.getCartsProductsKey(), value -> new ArrayList<>()).add(exchangeShowOnPurchasesDTO);

                }

            }

        }

        model.addAttribute("productIdExchangeInfo", productIdExchangeInfo);

        return "clientProfilePurchases";
    }

    // Método para filtrar as compras do cliente pela data de compra.
    @GetMapping("/clientProfilePurchasesFilter/{id}")
    public String getProfile(@PathVariable long id,
                             @RequestParam(value = "filterDate", required = false) String filterDate,
                             Model model, RedirectAttributes redirectAttributes) {

        // Carrega todas as compras do cliente específico
        List<Sale> clientSales = saleService.getClientSalesByClientId(id);

        if (filterDate != null && !filterDate.isEmpty()) {
            System.out.println("condition is going");
            // Converte a data recebida no formato String para LocalDate
            LocalDate date = LocalDate.parse(filterDate);

            // Filtra as compras do cliente pela data, ignorando a parte da hora
            clientSales = clientSales.stream()
                    .filter(sale -> sale.getDateTimeSale().toLocalDate().equals(date))  // Filtra pela data
                    .collect(Collectors.toList());

            // Passa o filtro de data para o modelo para exibição
            redirectAttributes.addFlashAttribute("filterDate", filterDate);
        }

        System.out.println("condition maybe went");

        // Passa as compras filtradas para o modelo
        //model.addAttribute("sales", clientSales);
        //model.addAttribute("id", id);  // Passa o id do cliente para a view
        redirectAttributes.addFlashAttribute("sales", clientSales);

        return "redirect:/clientProfilePurchases/" + id;  // Retorna a página Thymeleaf
    }

    @GetMapping("/clientProfileExchanges/{id}")
    public String seeClientProfileExchanges(@PathVariable long id, Model model) {

        List<Sale> clientSales = saleService.getClientSalesByClientId(id);

        int notificationQuantity = notificationService.getClientNotifications(id).size();
        model.addAttribute("notificationQuantity", notificationQuantity);

        List<Exchange> clientExchanges = (List<Exchange>) model.asMap().get("exchanges");

        if(clientExchanges == null) {
            List<Exchange> clientExchangesGetNormally = new ArrayList<>();

            for(Sale currentSale : clientSales) {

                clientExchangesGetNormally.addAll(currentSale.getExchangeList());

            }

            clientExchanges = clientExchangesGetNormally;
        }

        model.addAttribute("id", id);
        model.addAttribute("exchanges", clientExchanges);
        model.addAttribute("exchangeStatus", ExchangeStatusEnum.values());
        model.addAttribute("solicitadaStatus", ExchangeStatusEnum.TROCA_SOLICITADA);
        model.addAttribute("naoAutorizadaStatus", ExchangeStatusEnum.TROCA_NAO_AUTORIZADA);
        model.addAttribute("autorizadaStatus", ExchangeStatusEnum.TROCA_AUTORIZADA);
        model.addAttribute("emTrocaStatus", ExchangeStatusEnum.EM_TROCA);
        model.addAttribute("recebidaStatus", ExchangeStatusEnum.TROCA_RECEBIDA);
        model.addAttribute("trocaFinalizadaStatus", SaleStatusEnum.TROCA_FINALIZADA);
        model.addAttribute("client", clientService.getClientById(id));

        return "clientProfileExchanges";
    }

    // Método para filtrar as compras do cliente pela data de compra.
    @GetMapping("/clientProfileExchangesFilter/{id}")
    public String filterExchanges(@PathVariable long id,
                             @RequestParam(value = "filterDate", required = false) String filterDate,
                             Model model, RedirectAttributes redirectAttributes) {
        List<Sale> clientSales = saleService.getClientSalesByClientId(id);

        // Carrega todas as trocas do cliente específico
        List<Exchange> clientExchanges = new ArrayList<>();
        for(Sale currentSale : clientSales) {

            clientExchanges.addAll(currentSale.getExchangeList());

        }

        if (filterDate != null && !filterDate.isEmpty()) {
            System.out.println("condition is going");
            // Converte a data recebida no formato String para LocalDate
            LocalDate date = LocalDate.parse(filterDate);

            // Filtra as trocas do cliente pela data, ignorando a parte da hora
            clientExchanges = clientExchanges.stream()
                    .filter(exchange -> exchange.getExchangeRequestTime().toLocalDate().equals(date))  // Filtra pela data
                    .collect(Collectors.toList());

            // Passa o filtro de data para o modelo para exibição
            redirectAttributes.addFlashAttribute("filterDate", filterDate);
        }

        System.out.println("condition maybe went");

        // Passa as trocas filtradas para o modelo
        //model.addAttribute("sales", clientSales);
        //model.addAttribute("id", id);  // Passa o id do cliente para a view
        redirectAttributes.addFlashAttribute("exchanges", clientExchanges);

        return "redirect:/clientProfileExchanges/" + id;  //
    }

    @GetMapping("/clientProfileAddresses/{id}")
    public String seeClientProfileAddresses(@PathVariable long id, Model model) {
        List<Address> clientAddresses = clientService.getClientAddresses(id);
        int notificationQuantity = notificationService.getClientNotifications(id).size();
        model.addAttribute("notificationQuantity", notificationQuantity);

        model.addAttribute("id", id);
        model.addAttribute("clientId", id);
        model.addAttribute("addresses", clientAddresses);

        return "clientProfileAddresses";

    }

    @GetMapping("/clientProfileCreditCards/{id}")
    public String seeClientProfileCreditCards(@PathVariable long id, Model model) {
        List<CreditCard> clientCreditCards = clientService.getClientCreditCards(id);
        int notificationQuantity = notificationService.getClientNotifications(id).size();
        model.addAttribute("notificationQuantity", notificationQuantity);


        model.addAttribute("id", id);
        model.addAttribute("creditCards", clientCreditCards);

        return "clientProfileCreditCards";

    }

    @GetMapping("/creditCardUpdateBalance/{creditCardId}")
    public String updateCreditCardBalanceGet(@PathVariable long creditCardId, Model model) {
        CreditCard creditCard = creditCardService.getCreditCardById(creditCardId);
        ChangeCreditCardBalanceDTO changeCreditCardBalanceDTO = new ChangeCreditCardBalanceDTO();
        changeCreditCardBalanceDTO.setBalance(creditCard.getBalance());

        model.addAttribute("creditCard", creditCard);
        model.addAttribute("changeCreditCardBalanceDTO", changeCreditCardBalanceDTO);

        return "changeCreditCardBalance";
    }

    @PutMapping("/creditCardUpdateBalance/{creditCardId}")
    public String updateCreditCardBalance(@PathVariable long creditCardId, @ModelAttribute ChangeCreditCardBalanceDTO changeCreditCardBalanceDTO) {
        CreditCard creditCard = creditCardService.getCreditCardById(creditCardId);

        creditCard.setBalance(changeCreditCardBalanceDTO.getBalance());
        creditCardService.saveCreditCard(creditCard);

        return "redirect:/showAllClients";
    }

    @GetMapping("seeClientSales/{clientId}")
    @ResponseBody
    public List<ShowSaleOnClientCrudModalDTO> getAllClientSales(@PathVariable long clientId) {
        List<Sale> clientSales = saleService.getClientSalesByClientId(clientId);
        List<ShowSaleOnClientCrudModalDTO> dtoList = new ArrayList<>();

        for(Sale currentSale: clientSales) {
            ShowSaleOnClientCrudModalDTO showSaleOnClientCrudModalDTO = new ShowSaleOnClientCrudModalDTO();
            showSaleOnClientCrudModalDTO.setSaleId(currentSale.getId());
            showSaleOnClientCrudModalDTO.setSaleCode(currentSale.getSaleCode());
            showSaleOnClientCrudModalDTO.setSaleFinalPrice(currentSale.getFinalPrice());
            showSaleOnClientCrudModalDTO.setSaleStatusEnum(currentSale.getStatus());
            showSaleOnClientCrudModalDTO.setDeliveryAddressNickname(currentSale.getDeliveryAddress().getNickname());
            showSaleOnClientCrudModalDTO.setFreight(currentSale.getFreight());
            showSaleOnClientCrudModalDTO.setDateTimeSale(currentSale.getDateTimeSale());

            if(currentSale.getPromotionalCouponApplied() != null) {
                showSaleOnClientCrudModalDTO.setPromotionalCouponApplied(currentSale.getPromotionalCouponApplied().getCouponName());
            }

            for(SalesCards saleCard: currentSale.getCardsUsedInThisSale()) {
                SalesCardsClientCrudModalDTO salesCardsClientCrudModalDTO = new SalesCardsClientCrudModalDTO();
                salesCardsClientCrudModalDTO.setCreditCardNickname(saleCard.getCreditCard().getNickname());
                salesCardsClientCrudModalDTO.setAmountPaid(saleCard.getAmountPaid());

                showSaleOnClientCrudModalDTO.getSalesCardsList().add(salesCardsClientCrudModalDTO);
            }

            for(CartsProducts cartProduct: currentSale.getCart().getCartProducts()) {
                CartProductClientCrudModalDTO cartProductClientCrudModalDTO = new CartProductClientCrudModalDTO();
                cartProductClientCrudModalDTO.setProductName(cartProduct.getProduct().getName());
                cartProductClientCrudModalDTO.setProductQuantity(cartProduct.getProductQuantity());
                cartProductClientCrudModalDTO.setUnitaryPrice(cartProduct.getUnitaryPrice());
                cartProductClientCrudModalDTO.setFinalPrice(cartProduct.getFinalPrice());

                showSaleOnClientCrudModalDTO.getCartProductList().add(cartProductClientCrudModalDTO);
            }

            if(!currentSale.getExchangeCouponsApplied().isEmpty()) {
                for(ExchangeCoupon exchangeCoupon: currentSale.getExchangeCouponsApplied()) {
                    ExchangeCouponClientCrudModalDTO exchangeCouponClientCrudModalDTO = new ExchangeCouponClientCrudModalDTO();
                    exchangeCouponClientCrudModalDTO.setExchangeCouponCode(exchangeCoupon.getExchangeCouponCode());
                    exchangeCouponClientCrudModalDTO.setAmountWorth(exchangeCoupon.getAmountWorth());

                    showSaleOnClientCrudModalDTO.getExchangeCouponList().add(exchangeCouponClientCrudModalDTO);
                }

            }

            dtoList.add(showSaleOnClientCrudModalDTO);
        }

        for(ShowSaleOnClientCrudModalDTO dto: dtoList) {
            System.out.println(dto.getPromotionalCouponApplied());
            System.out.println(dto.getCartProductList());
            System.out.println(dto.getExchangeCouponList());
            System.out.println(dto.getSalesCardsList());
        }

        return dtoList;
    }






}