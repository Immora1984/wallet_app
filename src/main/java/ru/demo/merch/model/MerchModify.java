package ru.demo.merch.model;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        @Size(max = 3)
        private List<MultipartFile> photo = new ArrayList<>(3);
    }
}
