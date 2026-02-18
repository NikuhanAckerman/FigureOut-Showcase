package com.project.figureout.validation;

import com.project.figureout.dto.ProductDTO;
import com.project.figureout.model.PricingGroup;
import com.project.figureout.repository.PricingGroupRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

public class ProductPriceMustBeHigherThanPurchaseAmountValidator implements ConstraintValidator<ProductPriceMustBeHigherThanPurchaseAmount, Object>  {

    @Autowired
    private PricingGroupRepository pricingGroupRepository;

    @Override
    public void initialize(ProductPriceMustBeHigherThanPurchaseAmount constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {

        if(obj instanceof ProductDTO) {
            ProductDTO productDTO = (ProductDTO) obj;

            long pricingGroupId = productDTO.getPricingGroup();

            PricingGroup pricingGroup = pricingGroupRepository.findById(pricingGroupId).orElseThrow(() -> new NoSuchElementException("Grupo de precificação não encontrado com base no ID."));

            BigDecimal purchaseAmount = productDTO.getPurchaseAmount();
            BigDecimal price = productDTO.getPrice();
            BigDecimal pricingGroupMargin = pricingGroup.getPercentage();

            BigDecimal minPriceExpected = purchaseAmount.add(purchaseAmount.multiply(pricingGroupMargin));

            System.out.println(purchaseAmount);
            System.out.println(price);
            System.out.println(minPriceExpected);

            if(price.compareTo(minPriceExpected) < 0) {
                return false;
            } else {
                return true;
            }

        }

        return false;
    }

}
