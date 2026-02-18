package com.project.figureout.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ProductPriceMustBeHigherThanPurchaseAmountValidator.class)
public @interface ProductPriceMustBeHigherThanPurchaseAmount {
    String message() default "O preço do produto está abaixo da margem de lucro esperada. Aumente o preço.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
