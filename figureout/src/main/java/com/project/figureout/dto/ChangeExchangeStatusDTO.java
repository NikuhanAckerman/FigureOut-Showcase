package com.project.figureout.dto;

import com.project.figureout.model.ExchangeStatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangeExchangeStatusDTO {

    @NotBlank(message = "O status alterado não pode estar em branco.")
    private ExchangeStatusEnum status;

}
