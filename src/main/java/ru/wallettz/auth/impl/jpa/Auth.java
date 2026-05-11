package ru.wallettz.auth.impl.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.wallettz.user.model.User;

@Getter
@Setter
@Entity
@Table(name = "APP_AUTH")
public class Auth {
    @Id
    @GeneratedValue
    private UUID id;
    private String jti;
    @Version
    private LocalDateTime lastUse;
    @CreationTimestamp
    private LocalDateTime created;
    @ManyToOne
    private User user;
}
