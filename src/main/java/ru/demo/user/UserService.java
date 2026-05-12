package ru.demo.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.demo.user.model.*;
import ru.demo.user.model.UserModify.UserUpdate;

import java.util.UUID;

public interface UserService {

    Page<UserShort> searchBy(UserSearch search, UUID userId, Pageable page);

    UserDetail getById(UUID id);

    void deleteUser(UUID userId, boolean isAuth);

    void createVerify(UserCreate model);

    void userUpdate(UUID userId, UserUpdate request);
}
