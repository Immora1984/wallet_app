package ru.wallettz.auth;

import ru.wallettz.auth.impl.jpa.Auth;
import ru.wallettz.auth.model.AuthToken;
import ru.wallettz.user.model.User;

import java.util.function.Supplier;

public interface AuthMapper {

    Auth fromUser(User user);

    AuthToken toAuthToken(Auth auth, long expires, Supplier<String> accessToken);
}
