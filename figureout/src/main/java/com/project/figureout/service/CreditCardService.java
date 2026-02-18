package com.project.figureout.service;

import com.project.figureout.dto.CreditCardBrandDTO;
import com.project.figureout.dto.CreditCardDTO;
import com.project.figureout.model.Client;
import com.project.figureout.model.CreditCard;
import com.project.figureout.model.CreditCardBrand;
import com.project.figureout.repository.CreditCardBrandRepository;
import com.project.figureout.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class CreditCardService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardBrandRepository creditCardBrandRepository;

    public CreditCard getCreditCardById(long id) {
        return creditCardRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Cartão de crédito não encontrado com base no ID."));
    }

    public List<CreditCard> getClientCreditCards(long id) {
        return clientService.getClientById(id).getCreditCards();
    }

    public void saveCreditCard(CreditCard creditCard) {
        creditCardRepository.save(creditCard);
    }

    public void addCreditCardToClient(Client client, CreditCard creditCard) {
        client.getCreditCards().add(creditCard);
        creditCard.setClient(client);

        BigDecimal creditCardBalance = randomizeCreditCardBalance();
        System.out.println(creditCardBalance);

        creditCard.setBalance(creditCardBalance);
        creditCardRepository.save(creditCard);
    }

    public void deleteCreditCard(long id) {
        creditCardRepository.deleteById(id);
    }

    public List<CreditCardBrand> getAllCreditCardBrands() {
        return creditCardBrandRepository.findAll();
    }

    public CreditCardBrand getCreditCardBrandById(long id) {
        return creditCardBrandRepository.findById(id).get();
    }

    public void insertDataIntoCreditCard(CreditCard creditCard, CreditCardDTO creditCardDTO) {
        creditCard.setPreferential(creditCardDTO.isPreferential());
        creditCard.setNickname(creditCardDTO.getNickname());
        creditCard.setCardNumber(creditCardDTO.getCardNumber());
        creditCard.setPrintedName(creditCardDTO.getPrintedName());

        CreditCardBrand creditCardBrand = getCreditCardBrandById(creditCardDTO.getCreditCardBrandDTO().getId());

        creditCard.setBrand(creditCardBrand);
        creditCard.setSecurityCode(creditCardDTO.getSecurityCode());
    }

    public void registerCreditCard(Client client, CreditCardDTO creditCardDTO) {
        CreditCard creditCard = new CreditCard();

        insertDataIntoCreditCard(creditCard, creditCardDTO);



        addCreditCardToClient(client, creditCard);
    }

    public void updateCreditCard(CreditCard creditCard, CreditCardDTO creditCardDTO) {
        insertDataIntoCreditCard(creditCard, creditCardDTO);

        creditCardRepository.save(creditCard);
    }

    // Population Methods

    // DTO Population Methods

    public void populateCreditCardDTO(CreditCardDTO creditCardDTO, CreditCard creditCard) {

        creditCardDTO.setPreferential(creditCard.isPreferential());
        creditCardDTO.setNickname(creditCard.getNickname());
        creditCardDTO.setCardNumber(creditCard.getCardNumber());
        creditCardDTO.setPrintedName(creditCard.getPrintedName());

        CreditCardBrandDTO creditCardBrandDTO = new CreditCardBrandDTO();
        creditCardBrandDTO.setId(creditCard.getBrand().getId());
        creditCardDTO.setCreditCardBrandDTO(creditCardBrandDTO);

        creditCardDTO.setSecurityCode(creditCard.getSecurityCode());

    }

    public BigDecimal randomizeCreditCardBalance() {

        BigDecimal maxCreditCardBalance = BigDecimal.valueOf(20000);
        BigDecimal minCreditCardBalance = BigDecimal.valueOf(6000);

        Random random = new Random();
        double randomDouble = random.nextDouble();

        BigDecimal range = maxCreditCardBalance.subtract(minCreditCardBalance);
        BigDecimal randomValueInRange = minCreditCardBalance.add(range.multiply(BigDecimal.valueOf(randomDouble)));

        BigDecimal randomizedBalance = randomValueInRange.setScale(2, RoundingMode.HALF_UP);

        return randomizedBalance;

    }


}
