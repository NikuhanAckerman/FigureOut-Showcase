package com.project.figureout.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class AddressAtLeastOneTypeValidator implements ConstraintValidator<AddressAtLeastOneType, Object> {

    private String deliveryAddressFieldName;
    private String chargingAddressFieldName;

    @Override
    public void initialize(AddressAtLeastOneType constraintAnnotation) {
        this.deliveryAddressFieldName = "deliveryAddress";
        this.chargingAddressFieldName = "chargingAddress";
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {

        try {
            Field deliveryAddressField = obj.getClass().getDeclaredField(deliveryAddressFieldName);
            Field chargingAddressField = obj.getClass().getDeclaredField(chargingAddressFieldName);

            deliveryAddressField.setAccessible(true);
            chargingAddressField.setAccessible(true);

            boolean deliveryAddressValue = (boolean) deliveryAddressField.get(obj);
            boolean chargingAddressValue = (boolean) chargingAddressField.get(obj);

            return (deliveryAddressValue || chargingAddressValue);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
