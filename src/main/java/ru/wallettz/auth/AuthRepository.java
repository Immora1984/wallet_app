package ru.wallettz.auth;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.wallettz.auth.impl.jpa.Auth;

public interface AuthRepository extends JpaRepository<Auth, UUID> {
    @Query("select a from Auth a join fetch a.user where a.jti = :jti and a.user.id = :userId and a.user.enabled")
    Optional<Auth> findByJtiAndUserId(String jti, UUID userId);

    @Modifying
    @Query("delete from Auth a where a.jti = :jti")
    void removeByJti(String jti);
}