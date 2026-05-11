package ru.demo.auth.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.demo.user.model.Role;

@Getter
@Setter
public class AuthToken {
    private String accessToken;
    private String refreshToken;
    private List<Role> roles;
    private String tokenType;
    private long expiresIn;
}
