package com.project.figureout.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PhoneDTO {

    @NotNull(message = "O tipo de telefone (celular/fixo) não pode ser nulo.")
    private boolean cellphone;

    @NotBlank(message = "O DDD não pode estar em branco.")
    @Pattern(message= "O DDD é inválido.",regexp = "^[\\d]{2}$")
    private String ddd;

    @NotBlank(message = "O número de telefone não pode estar em branco.")
    @Pattern(message = "O número de telefone deve possuir apenas números, sem traços ou espaços.", regexp = "^[\\d]{8,9}$")
    //@Size(min = 8, max = 10)
    // 912345678
    // 12345678
    private String phoneNumber;

}
