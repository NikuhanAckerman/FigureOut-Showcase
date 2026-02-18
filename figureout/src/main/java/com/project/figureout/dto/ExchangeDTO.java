package com.project.figureout.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class ExchangeDTO {

    private HashMap<Long, Integer> cartProductQuantity;

}
