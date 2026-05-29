package ru.demo.merch.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MerchShort {
    private UUID id;
    private String size;
    private String band;
    private String color;
    private BigDecimal price;
    private List<String> photos;
    private List<Compound> compound;
    private String description;
}
