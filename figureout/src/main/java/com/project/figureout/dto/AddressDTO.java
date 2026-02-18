package com.project.figureout.dto;

import com.project.figureout.validation.AddressAtLeastOneType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AddressAtLeastOneType
public class AddressDTO {

    @NotNull(message = "O campo 'Entrega' não pode ser nulo.")
    private boolean deliveryAddress;

    @NotNull(message = "O campo 'Cobrança' não pode ser nulo.")
    private boolean chargingAddress;

    @NotBlank(message = "O apelido não pode estar vazio.")
    private String nickname;

    @NotBlank(message = "O tipo de residência não pode estar vazio.")
    @Pattern(message = "Tipo de residência inválido. Não insira números e nem caracteres especiais.", regexp = "^[A-Za-zÀ-ÖØ-Ýà-öø-ÿ\\s]*$") // letras, espaços
    private String typeOfResidence;

    @NotBlank(message = "O logradouro não pode estar vazio.")
    private String addressing;

    @NotBlank(message = "O número de residência não pode estar vazio.")
    //@Size(min = 1, max = 5)
    @Pattern(message = "Número de residência inválido. Insira somente números.", regexp = "^[\\d]{1,5}$") // 1 a 5 caracteres, apenas numeros
    private String houseNumber;

    @NotBlank(message = "O bairro não pode estar vazio.")
    @Pattern(message = "Bairro inválido. Não insira caracteres especiais.", regexp = "^[A-Za-zÀ-ÖØ-Ýà-öø-ÿ\\s\\d]*$")
    private String neighbourhood;

    @NotBlank(message = "O tipo de logradouro não pode estar vazio.")
    @Pattern(message = "Tipo de logradouro inválido. Não insira números e nem caracteres especiais.", regexp = "^[A-Za-zÀ-ÖØ-Ýà-öø-ÿ\\s]*$")
    private String addressingType;

    @NotBlank(message = "O CEP não pode estar vazio.")
    @Pattern(message = "O formato é inválido. O CEP deve estar no formato: '12345-678'.", regexp = "^\\d{5}-\\d{3}$") // 5 digitos, traço, 3 digitos
    private String cep;

    @NotBlank(message = "A cidade não pode estar vazia.")
    @Pattern(message = "Cidade inválida. Não insira números e nem caracteres especiais.", regexp = "^[A-Za-zÀ-ÖØ-Ýà-öø-ÿ\\s]*$")
    private String city;

    @Valid
    private StateDTO stateDTO;

    @Valid
    private CountryDTO countryDTO;

    private String observation;

}
