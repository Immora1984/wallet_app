package ru.demo.auth;

import ru.demo.auth.impl.jpa.Auth;
import ru.demo.auth.model.AuthToken;
import ru.demo.user.model.User;

import java.util.function.Supplier;

public interface AuthMapper {

    Auth fromUser(User user);

    AuthToken toAuthToken(Auth auth, long expires, Supplier<String> accessToken);
}
