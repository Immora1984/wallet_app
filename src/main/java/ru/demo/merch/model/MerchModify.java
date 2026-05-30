package ru.demo.merch.model;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MerchModify {

    @Getter
    @Setter
    public static class MerchUpdate {

        private String size;
        private String band;
        private String color;
        private BigDecimal price;
        private String description;
        private List<Compound> compound;
    }
}
