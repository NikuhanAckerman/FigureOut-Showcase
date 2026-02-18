package com.project.figureout.dto;

import com.project.figureout.model.SaleStatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangeSaleStatusDTO {

    @NotBlank(message = "O status alterado não pode estar em branco.")
    private SaleStatusEnum status;

}
