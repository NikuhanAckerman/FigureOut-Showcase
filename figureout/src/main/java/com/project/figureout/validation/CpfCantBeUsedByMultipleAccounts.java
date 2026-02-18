package com.project.figureout.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CpfCantBeUsedByMultipleAccountsValidator.class)
public @interface CpfCantBeUsedByMultipleAccounts {
    String message() default "Este CPF está indisponível. Use outro CPF.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
