package ru.wallettz.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ActionRequest {
    @NotNull
    private UUID walletId;
    @NotNull
    private OperationType operationType;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}

