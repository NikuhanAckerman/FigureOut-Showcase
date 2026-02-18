package com.project.figureout.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = OldPasswordCorrectValidator.class)
public @interface OldPasswordCorrect {
    String message() default "Esta senha não corresponde a senha atual.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
