package com.project.figureout.validation;

import com.project.figureout.dto.ProductDTO;
import com.project.figureout.model.Product;
import com.project.figureout.service.ProductService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NameCantBeUsedByMultipleProductsValidator implements ConstraintValidator<NameCantBeUsedByMultipleProducts, Object> {

    @Autowired
    private ProductService productService;

    @Override
    public void initialize(NameCantBeUsedByMultipleProducts constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {

        if(obj instanceof ProductDTO) {
            ProductDTO productDTO = (ProductDTO) obj;

            List<Product> productList = productService.getAllProducts();

            if(productDTO.getProductId() != 0) { // if its an update
                Product product = productService.getProductById(productDTO.getProductId());

                for(Product currentProduct : productList) {

                    if(productDTO.getName().equals(currentProduct.getName()) && !currentProduct.equals(product)) {
                        return false;
                    }

                }

                return true;
            }

            for(Product currentProduct : productList) {

                if(productDTO.getName().equals(currentProduct.getName())) {
                    return false;
                }

            }

            return true;
        }

       return false;
    }

}
