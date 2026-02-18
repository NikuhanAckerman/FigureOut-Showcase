package com.project.figureout.service;

import com.project.figureout.dto.ChangeExchangeStatusDTO;
import com.project.figureout.dto.ChangeSaleStatusDTO;
import com.project.figureout.dto.NotificationDTO;
import com.project.figureout.model.*;
import com.project.figureout.repository.CartRepository;
import com.project.figureout.repository.ProductRepository;
import com.project.figureout.repository.SaleRepository;
import com.project.figureout.repository.SalesCardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SalesCardsRepository salesCardsRepository;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private NotificationService notificationService;

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Sale getSaleById(long id) {
        return saleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Venda não encontrada com base no ID."));
    }

    public List<Sale> getSalesInsideDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Sale> allSales = getAllSales();
        List<Sale> salesInsideDesiredRange = new ArrayList<>();

        for(Sale currentSale : allSales) {
            LocalDateTime currentSaleDateTime = currentSale.getDateTimeSale();

            if(currentSaleDateTime.isAfter(startDate) && currentSaleDateTime.isBefore(endDate)) {

                salesInsideDesiredRange.add(currentSale);
            }
        }
        return salesInsideDesiredRange;
    }

    public List<Sale> getClientSalesByClientId(long id) {

        List<Sale> allSales = getAllSales();
        List<Sale> clientSales = new ArrayList<>();

        for(Sale sale : allSales) {

            Cart saleCart = sale.getCart();
            long saleCartClientId = saleCart.getClient().getId();

            if(saleCartClientId == id) {
                clientSales.add(sale);
            }

        }

        for(Sale sale: clientSales) {
            System.out.println(sale.getSaleCode());
            System.out.println(sale.getDateTimeSale().toString());
        }

        return clientSales;

    }

    public void saveSale(Sale sale) {
        saleRepository.save(sale);
    }

    public void changeSaleStatus(Sale sale, ChangeSaleStatusDTO changeSaleStatusDTO) throws IOException {
        SaleStatusEnum changeSaleStatusDTOStatus = changeSaleStatusDTO.getStatus();

        SaleStatusEnum trocaSolicitada = SaleStatusEnum.TROCA_SOLICITADA;
        SaleStatusEnum trocaNaoAutorizada = SaleStatusEnum.TROCA_NAO_AUTORIZADA;
        SaleStatusEnum trocaAutorizada = SaleStatusEnum.TROCA_AUTORIZADA;
        SaleStatusEnum emTroca = SaleStatusEnum.EM_TROCA;
        SaleStatusEnum trocaRecebida = SaleStatusEnum.TROCA_RECEBIDA;
        SaleStatusEnum trocaFinalizada = SaleStatusEnum.TROCA_FINALIZADA;

        List<Enum> exchangeEnumerations = new ArrayList<>();
        exchangeEnumerations.add(trocaSolicitada);
        exchangeEnumerations.add(trocaNaoAutorizada);
        exchangeEnumerations.add(trocaAutorizada);
        exchangeEnumerations.add(emTroca);
        exchangeEnumerations.add(trocaRecebida);
        exchangeEnumerations.add(trocaFinalizada);

        Exchange currentSaleExchange = null;

        for(Exchange currentExchange: sale.getExchangeList()) {

            if(currentExchange.isCurrentExchange()) {

                currentSaleExchange = currentExchange;
                break;
            }

        }

        if(currentSaleExchange != null) {

            ChangeExchangeStatusDTO changeExchangeStatusDTO = new ChangeExchangeStatusDTO();
            changeExchangeStatusDTO.setStatus(ExchangeStatusEnum.valueOf(changeSaleStatusDTOStatus.name()));
            System.out.println(changeSaleStatusDTOStatus.name());

            if(exchangeEnumerations.contains(changeSaleStatusDTOStatus)) {
                exchangeService.changeExchangeStatus(currentSaleExchange, changeExchangeStatusDTO);

                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setCategory(NotificationCategoryEnum.VENDA);
                notificationDTO.setTitle("O status da venda de código '" + sale.getSaleCode() + "' foi alterado.");
                notificationDTO.setDescription("O status desta venda foi alterado de " + sale.getStatus().name() + " para " + changeSaleStatusDTO.getStatus().name() + ".");
                notificationService.createNotification(sale.getCart().getClient(), notificationDTO);

                sale.setStatus(changeSaleStatusDTOStatus);

                saveSale(sale);
            }

            return;
        }

        if(!exchangeEnumerations.contains(changeSaleStatusDTOStatus)) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setCategory(NotificationCategoryEnum.VENDA);
            notificationDTO.setTitle("O status da venda de código '" + sale.getSaleCode() + "' foi alterado.");
            notificationDTO.setDescription("O status desta venda foi alterado de " + sale.getStatus().name() + " para " + changeSaleStatusDTO.getStatus().name() + ".");
            notificationService.createNotification(sale.getCart().getClient(), notificationDTO);

            sale.setStatus(changeSaleStatusDTOStatus);

            saveSale(sale);
        }

    }

}
