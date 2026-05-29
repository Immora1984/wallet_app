package ru.demo.merch.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MerchDetail {
    private UUID id;
    private List<String> photos;
    private String size;
    private String band;
    private String color;
    private BigDecimal price;
    private List<Compound> compound;
}
