package com.project.figureout.controller;

import com.project.figureout.dto.ChangeExchangeStatusDTO;
import com.project.figureout.dto.ExchangeDTO;
import com.project.figureout.dto.NotificationDTO;
import com.project.figureout.model.*;
import com.project.figureout.repository.CartsProductsRepository;
import com.project.figureout.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/exchange")
public class ExchangeController {

    @Autowired
    SaleService saleService;

    @Autowired
    CartsProductsRepository cartsProductsRepository;

    @Autowired
    ExchangeService exchangeService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/requestExchange/{id}")
    public String requestExchangeGet(@PathVariable long id, Model model) {
        Sale sale = saleService.getSaleById(id);
        Client client = sale.getCart().getClient();

        model.addAttribute("sale", sale);
        model.addAttribute("clientId", client.getId());
        model.addAttribute("exchangeDTO", new ExchangeDTO());

        return "requestExchange";
    }

    @PostMapping("/requestExchange/{id}")
    public String requestExchangePost(@PathVariable long id, @ModelAttribute ExchangeDTO exchangeDTO, Model model,
                                      HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Sale sale = saleService.getSaleById(id);
        Client client = sale.getCart().getClient();
        SaleStatusEnum status = sale.getStatus();
        SaleStatusEnum deliveredStatus = SaleStatusEnum.ENTREGUE;
        SaleStatusEnum exchangeEndedStatus = SaleStatusEnum.TROCA_FINALIZADA;
        SaleStatusEnum exchangeNotAuthorizedStatus = SaleStatusEnum.TROCA_NAO_AUTORIZADA;

        List<String> errors = new ArrayList<>();

        boolean canRequestExchange = true;

        for(Exchange currentExchange: sale.getExchangeList()) {

            if(currentExchange.isCurrentExchange()) {
                canRequestExchange = false;
                break;
            }

        }

        if(!(sale.getStatus().equals(deliveredStatus) || sale.getStatus().equals(exchangeEndedStatus))) {
            errors.add("Não é possível realizar uma troca envolvendo essa compra por conta do status da compra.");
        }

        if(canRequestExchange) {

            exchangeDTO.getCartProductQuantity().forEach((key, value) -> {
                CartsProducts cartProduct = cartsProductsRepository.getCartsProductsByProductIdAndCart(key, sale.getCart());
                String name = cartProduct.getProduct().getName();

                if(value < 0) {
                    errors.add("Não se pode trocar menos que 0 de '" + name + "'.");
                }
                if(value > cartProduct.getExchangeableQuantity()) {
                    errors.add("Não é possível trocar essa quantidade do produto '" + name + "' pois a quantidade disponível para troca é menor do que a digitada.");
                }
            });

            if(exchangeDTO.getCartProductQuantity().values().stream().allMatch(value -> value == 0)) {
                errors.add("Não é possível realizar uma troca de 0 produtos no total.");
            }

            if(!errors.isEmpty()) {
                String referer = request.getHeader("Referer");

                if(!errors.isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorsExchangeRequest", errors);

                    return "redirect:" + referer;
                }
            }

            Exchange newExchange = new Exchange();
            newExchange.setSale(sale);
            newExchange.setClient(client);

            if(status == deliveredStatus || status == exchangeEndedStatus || status == exchangeNotAuthorizedStatus) {
                sale.setStatus(SaleStatusEnum.TROCA_SOLICITADA);
                newExchange.setStatus(ExchangeStatusEnum.TROCA_SOLICITADA);
            } else {
                return "redirect:/clientProfilePurchases/" + client.getId();
            }

            HashMap<Long, Integer> exchangeMap = exchangeDTO.getCartProductQuantity();

            char[][] allowedCharacterRanges = {{'a','z'},{'A','Z'},{'0','9'}};

            RandomStringGenerator generator = new RandomStringGenerator.Builder()
                    .withinRange(allowedCharacterRanges)
                    .build();

            String exchangeCode = "#" + generator.generate(6);

            newExchange.setExchangeCode(exchangeCode);

            exchangeService.saveExchange(newExchange);

            exchangeMap.forEach((productId, quantity) -> {// Assuming constructor that takes a Long
                CartsProducts cartProduct = cartsProductsRepository.getCartsProductsByProductIdAndCart(productId, sale.getCart());

                ExchangeProductsKey exchangeProductsKey = new ExchangeProductsKey();
                exchangeProductsKey.setExchangeId(newExchange.getId());

                CartsProductsKey cartsProductsKey = new CartsProductsKey();
                cartsProductsKey.setCartId(cartProduct.getId().getCartId());
                cartsProductsKey.setProductId(cartProduct.getId().getProductId());

                exchangeProductsKey.setCartsProductsKey(cartsProductsKey);

                ExchangeProducts exchangeProduct = new ExchangeProducts();
                exchangeProduct.setId(exchangeProductsKey);
                exchangeProduct.setCartProduct(cartProduct);
                exchangeProduct.setQuantityReturned(quantity);
                exchangeProduct.setFinalAmount(exchangeProduct.getCartProduct().getUnitaryPrice().multiply(BigDecimal.valueOf(exchangeProduct.getQuantityReturned())));
                exchangeProduct.setExchange(newExchange);

                newExchange.getReturnedProducts().add(exchangeProduct);
            });

            newExchange.setExchangeRequestTime(LocalDateTime.now());
            newExchange.setCurrentExchange(true);

            BigDecimal finalAmountExchange = BigDecimal.valueOf(0);

            for(ExchangeProducts exchangeProduct: newExchange.getReturnedProducts()) {

                finalAmountExchange = finalAmountExchange.add(exchangeProduct.getFinalAmount());

            }

            sale.setStatus(SaleStatusEnum.TROCA_SOLICITADA);
            newExchange.setStatus(ExchangeStatusEnum.TROCA_SOLICITADA);
            newExchange.setFinalAmount(finalAmountExchange);

            exchangeService.saveExchange(newExchange);

            sale.getExchangeList().add(newExchange);

            saleService.saveSale(sale);

            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setCategory(NotificationCategoryEnum.TROCA);
            notificationDTO.setTitle("Uma troca foi requisitada!");
            notificationDTO.setDescription("Uma troca de código '" + newExchange.getExchangeCode() + "' foi requisitada.");
            notificationService.createNotification(sale.getCart().getClient(), notificationDTO);

        } else {
            errors.add("Você não pode requisitar uma troca envolvendo esta compra atualmente.");

            String referer = request.getHeader("Referer");
            redirectAttributes.addFlashAttribute("errorsExchangeRequest", errors);
            return "redirect:" + referer;
        }

        return "redirect:/clientProfileExchanges/" + client.getId();
    }

    @PutMapping("/seeExchanges/changeExchangeStatus/{exchangeId}")
    public String changeExchangeStatus(@PathVariable long exchangeId, @ModelAttribute ChangeExchangeStatusDTO changeExchangeStatusDTO) throws IOException {
        // Chama o serviço saleService para alterar o status da venda, passando a venda encontrada pelo ID e os dados do DTO
        exchangeService.changeExchangeStatus(exchangeService.getExchangeById(exchangeId), changeExchangeStatusDTO);

        // Após alterar o status da venda, redireciona o usuário para a página "/sales/seeSales"
        return "redirect:/sales/seeSales";
    }




}
