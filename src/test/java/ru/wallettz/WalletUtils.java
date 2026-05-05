package ru.wallettz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.wallettz.dto.ActionRequest;
import ru.wallettz.entity.Wallet;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletUtils {
    public static Wallet generateWallet() {
        var wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setBalance(BigDecimal.valueOf(10000.00));
        return wallet;
    }

    public static byte[] getActionRequestBody() throws JsonProcessingException {
        var request = new ActionRequest();
        request.setWalletId(UUID.randomUUID());
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(BigDecimal.valueOf(10.00));
        return (new ObjectMapper()).writeValueAsBytes(request);
    }
}
