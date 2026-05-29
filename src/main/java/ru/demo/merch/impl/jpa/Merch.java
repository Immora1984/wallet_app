package ru.demo.merch.impl.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import ru.demo.merch.model.Compound;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "APP_MERCH")
public class Merch {
    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime modified;

    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> photos;

    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<Compound> compound;

    private String size;
    private String band;
    private String color;
    private BigDecimal price;
    private String description;

}
