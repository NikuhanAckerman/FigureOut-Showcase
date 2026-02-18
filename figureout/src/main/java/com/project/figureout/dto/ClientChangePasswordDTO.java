package com.project.figureout.dto;

import com.project.figureout.validation.OldPasswordCorrect;
import com.project.figureout.validation.PasswordsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@PasswordsMatch
@OldPasswordCorrect
public class ClientChangePasswordDTO {

    private long clientId;

    @NotBlank(message = "O campo de 'Senha Atual' não pode estar em branco.")
    private String oldPassword;

    @NotBlank(message = "O campo de 'Nova Senha' não pode estar em branco.")
    @Pattern(message = "A senha digitada é inválida.", regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\p{Punct})[A-Za-z\\d\\p{Punct}]{8,}$")
    private String newPassword;

    @NotBlank(message = "A confirmação da nova senha não pode estar em branco.")
    @Pattern(message = "A senha digitada é inválida.", regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\p{Punct})[A-Za-z\\d\\p{Punct}]{8,}$")
    private String confirmPassword;

}
