package ru.wallettz.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.wallettz.entity.Role;

@Getter
@Setter
public class AuthTokenResponse {
    private String accessToken;
    private String refreshToken;
    private List<Role> roles;
    private String tokenType;
    private long expiresIn;
}
