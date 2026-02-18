package com.project.figureout.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NameCantBeUsedByMultipleProductsValidator.class)
public @interface NameCantBeUsedByMultipleProducts {
    String message() default "Este nome já está em outro produto.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
