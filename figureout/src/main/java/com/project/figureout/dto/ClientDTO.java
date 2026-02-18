package com.project.figureout.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClientDTO {

    @Valid
    private ClientBasicDataDTO clientBasicDataDTO;

    @Valid
    private AddressDTO addressDTO;

}