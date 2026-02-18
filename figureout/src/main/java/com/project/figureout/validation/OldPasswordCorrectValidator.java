package com.project.figureout.validation;

import com.project.figureout.dto.ClientChangePasswordDTO;
import com.project.figureout.model.Client;
import com.project.figureout.service.ClientService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class OldPasswordCorrectValidator implements ConstraintValidator<OldPasswordCorrect, Object> {

    @Autowired
    ClientService clientService;

    @Override
    public void initialize(OldPasswordCorrect constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        if(obj instanceof ClientChangePasswordDTO) {
            ClientChangePasswordDTO clientChangePasswordDTO = (ClientChangePasswordDTO) obj;

            Client client = clientService.getClientById(clientChangePasswordDTO.getClientId());

            try {
                if(client.getPassword().equals(clientChangePasswordDTO.getOldPassword())) {
                    System.out.println("The old password is equal to the DTO (doesnt make sense");
                    return true;
                } else {
                    constraintValidatorContext.disableDefaultConstraintViolation();

                    constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                            .addPropertyNode("oldPassword").addConstraintViolation();

                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return false;

    }

}
