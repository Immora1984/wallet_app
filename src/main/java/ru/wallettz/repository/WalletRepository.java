package ru.wallettz.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.wallettz.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {}