package com.project.figureout.dto;

import com.project.figureout.validation.NameCantBeUsedByMultipleProducts;
import com.project.figureout.validation.ProductPriceMustBeHigherThanPurchaseAmount;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@NameCantBeUsedByMultipleProducts
@ProductPriceMustBeHigherThanPurchaseAmount
public class ProductDTO {

    private long productId;

    @NotNull
    private boolean active = true;

    private String reasonForInactivationOrActivation;

    @NotBlank(message = "O nome do produto não pode estar em branco.")
    private String name;

    private String description;

    @NotNull(message = "O campo de altura do produto não pode ser nulo.")
    @Positive(message = "A altura do produto deve ser maior que zero.")
    private Float height;

    @NotNull(message = "O campo de largura do produto não pode ser nulo.")
    @Positive(message = "A largura do produto deve ser maior que zero.")
    private Float width;

    @NotNull(message = "O campo de peso do produto não pode ser nulo.")
    @Positive(message = "O peso do produto deve ser maior que zero.")
    private Float weight;

    @NotNull(message = "O campo de comprimento do produto não pode ser nulo.")
    @Positive(message = "O comprimento do produto deve ser maior que zero.")
    private Float length;

    @NotNull(message = "O valor de compra do produto não pode ser nulo")
    @Positive(message = "O valor de compra deve ser maior que zero.")
    private BigDecimal purchaseAmount;

    @NotNull(message = "O(s) id(s) de categoria não deve(m) ser nulo(s).")
    private List<Long> categoriesIds;

    @NotNull(message = "O id do grupo de precificação não pode ser nulo.")
    private long pricingGroup;

    @NotNull(message = "O preço de venda do produto não pode ser nulo.")
    @PositiveOrZero(message = "O preço de venda do produto não deve ser menor que zero.")
    private BigDecimal price;

    private MultipartFile productImage;

    @Valid
    private StockDTO stockDTO;

    @NotNull(message = "O id do fornecedor não pode ser nulo.")
    private long supplier;

    @NotNull(message = "O fabricante não pode ser nulo.")
    private long manufacturer;

    @NotNull(message = "O tamanho do produto não pode ser nulo.")
    private long size;

}
