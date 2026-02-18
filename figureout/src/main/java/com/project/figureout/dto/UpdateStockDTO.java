package com.project.figureout.dto;

import com.project.figureout.model.Supplier;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class UpdateStockDTO {

    @Positive(message = "A quantidade disponível no estoque não pod ser menor que 0.")
    private int productQuantityAvailable;

    @PastOrPresent(message = "A data de mudança deve estar no passado ou no presente.")
    @NotNull(message = "O campo de data de mudança não pode ser nulo.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateChangeOfStockQuantity;

    private List<Long> supplier = new ArrayList<>();

}
