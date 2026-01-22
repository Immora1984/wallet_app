package ru.wallettz.service;

import ru.wallettz.dto.ActionRequest;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {
    BigDecimal getBalance(UUID walletId);

    void doOperation(ActionRequest request);
}