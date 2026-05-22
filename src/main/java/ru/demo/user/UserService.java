package ru.demo.user;

import ru.demo.user.model.*;
import ru.demo.user.model.UserModify.UserUpdate;

import java.util.UUID;

public interface UserService {

    UserDetail getById(UUID id);

    void deleteUser(UUID userId, boolean isAuth);

    void userUpdate(UUID userId, UserUpdate request);

    void registerUser(UserCreate userCreate);
}
