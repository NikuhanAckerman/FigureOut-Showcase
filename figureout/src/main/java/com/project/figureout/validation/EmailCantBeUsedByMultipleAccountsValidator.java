package com.project.figureout.validation;

import com.project.figureout.dto.ClientBasicDataDTO;
import com.project.figureout.model.Client;
import com.project.figureout.service.ClientService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EmailCantBeUsedByMultipleAccountsValidator implements ConstraintValidator<EmailCantBeUsedByMultipleAccounts, Object> {

    @Autowired
    ClientService clientService;

    @Override
    public void initialize(EmailCantBeUsedByMultipleAccounts constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {

        System.out.println("ta rodando?");

        if(obj instanceof ClientBasicDataDTO) {
            ClientBasicDataDTO clientBasicDataDTO = (ClientBasicDataDTO) obj;

            long id = clientBasicDataDTO.getClientId();
            List<Client> allClients = clientService.getAllClients();

            System.out.println("Email: " + id);

            if(id != 0) { // if its update

                Client client = clientService.getClientById(id);

               for(Client currentClient: allClients) {

                    if(currentClient.getEmail().equals(clientBasicDataDTO.getEmail())) {

                        if(!currentClient.equals(client)) {
                            return false;
                        }

                    }

                }

                return true;

            } else {

                for(Client currentClient: allClients) {

                    if(currentClient.getEmail().equals(clientBasicDataDTO.getEmail())) {

                        return false;

                    }

                }


            }

        }

        return true;

    }
}
