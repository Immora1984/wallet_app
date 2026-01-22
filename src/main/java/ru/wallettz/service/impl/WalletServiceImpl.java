package ru.wallettz.service.impl;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wallettz.dto.ActionRequest;
import ru.wallettz.entity.Wallet;
import ru.wallettz.exception.WalletException;
import ru.wallettz.repository.WalletRepository;
import ru.wallettz.service.WalletService;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public BigDecimal getBalance(UUID walletId) {
        return walletRepository.findById(walletId).map(Wallet::getBalance).orElseThrow(WalletException.NotFound::new);
    }

    @Transactional
    public void doOperation(ActionRequest request) {
        var wallet = walletRepository.findById(request.getWalletId()).orElseThrow(WalletException.NotFound::new);
        var balance = wallet.getBalance();

        switch (request.getOperationType()) {
            case DEPOSIT:
                wallet.setBalance(balance.add(request.getAmount()));
                walletRepository.save(wallet);
                break;
            case WITHDRAW:
                if (balance.compareTo(request.getAmount()) < 0) {
                    throw new WalletException.NotEnoughBalance();
                }
                wallet.setBalance(balance.subtract(request.getAmount()));
                this.walletRepository.save(wallet);
        }
    }
}
