package ru.demo.user;

import ru.demo.user.impl.jpa.User;
import ru.demo.user.model.UserCreate;
import ru.demo.user.model.UserDetail;
import ru.demo.user.model.UserShort;

public interface UserMapper {

    UserShort toShort(User user);

    UserDetail toDetail(User user);

    User fromCreate(UserCreate userCreate);
}
