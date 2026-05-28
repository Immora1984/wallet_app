package ru.demo.user.impl.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.userdetails.UserDetails;
import ru.demo.user.model.Role;

@Entity
@Getter
@Setter
@Table(name = "APP_USER")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;
    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime modified;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(2003)
    private List<Role> authorities;
    private String username;
    private String password;
    private boolean enabled;
    @Email
    private String email;
}
