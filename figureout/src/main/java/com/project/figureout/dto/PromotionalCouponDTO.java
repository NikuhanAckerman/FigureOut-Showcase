package com.project.figureout.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PromotionalCouponDTO {

    @NotNull(message = "O id do cupom promnocional não pode ser nulo.")
    private long couponId;

    //@NotBlank(message = "O campo de cupom promocional não pode estar em branco.")
    private String couponName;

}
