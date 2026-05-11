package ru.wallettz.auth.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.wallettz.user.model.Role;

@Getter
@Setter
public class AuthToken {
    private String accessToken;
    private String refreshToken;
    private List<Role> roles;
    private String tokenType;
    private long expiresIn;
}
