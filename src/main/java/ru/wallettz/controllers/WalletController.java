package ru.wallettz.controllers;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.wallettz.dto.ActionRequest;
import ru.wallettz.service.WalletService;
import ru.wallettz.util.ApiOperation;

@Slf4j
@RestController
@RequestMapping({"/api/v1"})
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @ApiOperation(
            path = {"/wallets/{walletId}"},
            method = {RequestMethod.GET}
    )
    BigDecimal getBalance(@PathVariable("walletId") UUID walletId) {
        return walletService.getBalance(walletId);
    }

    @ApiOperation(
            path = {"/wallet"},
            method = {RequestMethod.POST}
    )
    void action(@RequestBody @Valid ActionRequest request) {
        walletService.doOperation(request);
    }
}
