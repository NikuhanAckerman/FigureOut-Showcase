package com.project.figureout.validation;

import com.project.figureout.dto.CreditCardDTO;
import com.project.figureout.model.Client;
import com.project.figureout.model.CreditCard;
import com.project.figureout.service.ClientService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OnlyOnePreferentialCreditCardValidator implements ConstraintValidator<OnlyOnePreferentialCreditCard, Object> {

    @Autowired
    ClientService clientService;

    @Override
    public void initialize(OnlyOnePreferentialCreditCard constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        if(obj instanceof CreditCardDTO){
            CreditCardDTO creditCardDTO = (CreditCardDTO) obj;

            if(creditCardDTO.isPreferential()) {
                long clientId = creditCardDTO.getClientId();
                long creditCardId = creditCardDTO.getCreditCardId();
                System.out.println(clientId);
                System.out.println(creditCardId);

                Client client = clientService.getClientById(clientId);

                List<CreditCard> clientCreditCardList = client.getCreditCards();

                long preferentialCount = clientCreditCardList.stream()
                        .filter(CreditCard::isPreferential)
                        .count();

                if(creditCardDTO.getCreditCardId() != 0) { // if its an update
                    CreditCard creditCardBeingUpdated = null;

                    for(CreditCard creditCard : clientCreditCardList){

                        if(creditCard.getId() == creditCardId){

                            creditCardBeingUpdated = creditCard;
                            break;

                        }

                    }

                    if(!creditCardBeingUpdated.isPreferential()) {

                        if(preferentialCount >= 1) {
                            return false;
                        }

                    }

                    return (preferentialCount <= 1);
                }

                return (preferentialCount < 1); // if its a create
            }

            return true;
        }

        return false;
    }

}
