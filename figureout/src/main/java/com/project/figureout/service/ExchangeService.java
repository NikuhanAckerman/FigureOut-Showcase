package com.project.figureout.service;

import com.project.figureout.dto.ChangeExchangeStatusDTO;
import com.project.figureout.dto.NotificationDTO;
import com.project.figureout.model.*;
import com.project.figureout.model.ExchangeProducts;
import com.project.figureout.repository.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Getter @Setter
public class ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private ExchangeCouponRepository exchangeCouponRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private NotificationService notificationService;

    private BigDecimal percentileToCreateNewExchangeCoupon = BigDecimal.valueOf(0.20); // if I use an exchange coupon and the
    @Autowired
    private SaleRepository saleRepository;
    // leftover value is bigger than 100% + (percentile*100)%
                                                                                     // then I create a new exchange coupon with the value of
                                                                                    // the leftover.

    public void saveExchange(Exchange exchange) {
        exchangeRepository.save(exchange);
    }

    public List<Exchange> getAllExchanges() {
        return exchangeRepository.findAll();
    }

    public Exchange getExchangeById(long id) {
        return exchangeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Troca não encontrada por ID."));
    }

    public void generateExchangeCoupon(Exchange exchange) {
        ExchangeCoupon newExchangeCoupon = new ExchangeCoupon();
        newExchangeCoupon.setClient(exchange.getClient());

        char[][] allowedCharacterRanges = {{'a','z'},{'A','Z'},{'0','9'}};

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(allowedCharacterRanges)
                .build();

        String couponCode = "#" + generator.generate(10);

        newExchangeCoupon.setExchangeCouponCode(couponCode);

        newExchangeCoupon.setAmountWorth(exchange.getFinalAmount());
        newExchangeCoupon.setUsed(false);

        exchangeCouponRepository.save(newExchangeCoupon);

        exchange.setCoupon(newExchangeCoupon);

        exchangeRepository.save(exchange);

        exchange.getClient().getExchangeCouponList().add(newExchangeCoupon);

    }

    public void generateExchangeCouponSurpass(Client client, BigDecimal amount) {
        ExchangeCoupon newExchangeCoupon = new ExchangeCoupon();
        newExchangeCoupon.setClient(client);

        char[][] allowedCharacterRanges = {{'a','z'},{'A','Z'},{'0','9'}};

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(allowedCharacterRanges)
                .build();

        String couponCode = "#" + generator.generate(10);

        newExchangeCoupon.setExchangeCouponCode(couponCode);

        newExchangeCoupon.setAmountWorth(amount);
        newExchangeCoupon.setUsed(false);

        exchangeCouponRepository.save(newExchangeCoupon);

        client.getExchangeCouponList().add(newExchangeCoupon);

    }

    public void changeExchangeStatus(Exchange exchange, ChangeExchangeStatusDTO changeExchangeStatusDTO) {
        ExchangeStatusEnum trocaSolicitada = ExchangeStatusEnum.TROCA_SOLICITADA;
        ExchangeStatusEnum trocaNaoAutorizada = ExchangeStatusEnum.TROCA_NAO_AUTORIZADA;
        ExchangeStatusEnum trocaAutorizada = ExchangeStatusEnum.TROCA_AUTORIZADA;
        ExchangeStatusEnum trocaRecebida = ExchangeStatusEnum.TROCA_RECEBIDA;
        ExchangeStatusEnum trocaFinalizada = ExchangeStatusEnum.TROCA_FINALIZADA;

        ExchangeStatusEnum dtoStatus = changeExchangeStatusDTO.getStatus();

        LocalDateTime now = LocalDateTime.now();

        List<ExchangeProducts> returnedProducts = exchange.getReturnedProducts();

        if(dtoStatus.equals(trocaSolicitada)) {
            exchange.setExchangeRequestTime(now);

        } else if(dtoStatus.equals(trocaAutorizada)) {
            exchange.setExchangeAcceptedTime(now);

        } else if (dtoStatus.equals(trocaFinalizada)) {
            exchange.setExchangeFinishTime(now);
            exchange.setCurrentExchange(false);

        } else if (dtoStatus.equals(trocaRecebida)) {

            HashMap<Stock, Integer> cartProductQuantityToAdd = new HashMap<>();

            for(CartsProducts cartProduct : exchange.getSale().getCart().getCartProducts()) {
                Stock stock = stockService.getProductInStockByProductId(cartProduct.getProduct().getId());

                for(ExchangeProducts currentExchangeProduct : returnedProducts) {

                    if(currentExchangeProduct.getCartProduct().getId() == cartProduct.getId()) {

                        cartProductQuantityToAdd.put(stock, currentExchangeProduct.getQuantityReturned());
                        cartProduct.setExchangeableQuantity(cartProduct.getExchangeableQuantity() - currentExchangeProduct.getQuantityReturned());

                    }

                }

            }

            stockService.addInStockList(cartProductQuantityToAdd);

            generateExchangeCoupon(exchange);

        } else if (dtoStatus.equals(trocaNaoAutorizada)) {

            for(ExchangeProducts currentProduct: returnedProducts) {

                currentProduct.setFinalAmount(BigDecimal.valueOf(0.00));
                currentProduct.setQuantityReturned(0);

            }

            exchange.setCurrentExchange(false);

        }

        exchange.setStatus(changeExchangeStatusDTO.getStatus());

        if(exchange.isCurrentExchange()) {
            Sale sale = exchange.getSale();

            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setCategory(NotificationCategoryEnum.VENDA);
            notificationDTO.setTitle("O status da venda de código '" + sale.getSaleCode() + "' foi alterado.");
            notificationDTO.setDescription("O status desta venda foi alterado de " + sale.getStatus().name() + " para " + exchange.getStatus());
            notificationService.createNotification(sale.getCart().getClient(), notificationDTO);

            sale.setStatus(SaleStatusEnum.valueOf(String.valueOf(exchange.getStatus())));

            saleRepository.save(sale);
        }

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setCategory(NotificationCategoryEnum.TROCA);
        notificationDTO.setTitle("O status da troca de código '" + exchange.getExchangeCode() + "' foi alterado.");
        notificationDTO.setDescription("O status desta troca foi alterado de " + exchange.getStatus().name() + " para " + changeExchangeStatusDTO.getStatus().name() + ".");
        notificationService.createNotification(exchange.getSale().getCart().getClient(), notificationDTO);

        saveExchange(exchange);
    }

    public List<Sale> findExchangesByDate(LocalDateTime date) {
        return exchangeRepository.findByExchangeRequestTime(date);
    }


}
