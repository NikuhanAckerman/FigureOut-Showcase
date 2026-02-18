package com.project.figureout.validation;

import com.project.figureout.dto.ClientBasicDataDTO;
import com.project.figureout.model.Client;
import com.project.figureout.service.ClientService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CpfCantBeUsedByMultipleAccountsValidator implements ConstraintValidator<CpfCantBeUsedByMultipleAccounts, Object> {

    @Autowired
    ClientService clientService;

    @Override
    public void initialize(CpfCantBeUsedByMultipleAccounts constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {

       if(obj instanceof ClientBasicDataDTO) {
            ClientBasicDataDTO clientBasicDataDTO = (ClientBasicDataDTO) obj;

            long id = clientBasicDataDTO.getClientId();
            List<Client> allClients = clientService.getAllClients();
            String treatedClientCpf = clientService.treatMaskedCpf(clientBasicDataDTO.getCpf());

           System.out.println("CPF: " + id);

            if(id != 0) { // if its update

                Client client = clientService.getClientById(id);

                for(Client currentClient: allClients) {

                    String currentClientCpf = clientService.treatMaskedCpf(currentClient.getCpf());

                    if(currentClientCpf.equals(treatedClientCpf)) {

                        if(!currentClient.equals(client)) {
                            return false;
                        }

                    }

               }

                return true;

           } else {

                for(Client currentClient: allClients) {

                    String currentClientCpf = clientService.treatMaskedCpf(currentClient.getCpf());

                    if(currentClientCpf.equals(treatedClientCpf)) {

                        return false;

                    }

                }


            }

       }

       return true;

    }
}
