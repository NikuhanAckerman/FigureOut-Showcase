package com.project.figureout.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EmailCantBeUsedByMultipleAccountsValidator.class)
public @interface EmailCantBeUsedByMultipleAccounts {
    String message() default "Este e-mail está indisponível. Use outro e-mail.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
