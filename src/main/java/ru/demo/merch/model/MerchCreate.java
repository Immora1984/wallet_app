package ru.demo.merch.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MerchCreate {
    @NotNull
    private String band;
    private String size;
    private String color;
    private BigDecimal price;
    private List<Compound> compound;
    private String description;
}
