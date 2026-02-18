package com.project.figureout.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = OnlyOnePreferentialCreditCardValidator.class)
public @interface OnlyOnePreferentialCreditCard {
    String message() default "Já existe um cartão de crédito como preferencial nesta conta.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
