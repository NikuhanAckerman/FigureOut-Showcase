package com.project.figureout.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AddressAtLeastOneTypeValidator.class)
public @interface AddressAtLeastOneType {
    String message() default "O endereço está registrado como sem nenhum tipo. Marque pelo menos um tipo.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
