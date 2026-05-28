package ru.demo.user;

import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.demo.user.impl.jpa.User;
import ru.demo.user.model.*;
import ru.demo.user.model.UserModify.UserUpdate;

import java.util.UUID;

public interface UserService {

    UserDetail getById(UUID id);

    void deleteUser(UUID userId, boolean isAuth);

    void userUpdate(UUID userId, UserUpdate request);

    void registerUser(UserCreate userCreate);

    User importOAuth2(OAuth2User oAuth2User);
}
