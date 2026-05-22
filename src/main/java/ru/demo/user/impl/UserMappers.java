package ru.demo.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.demo.user.UserMapper;
import ru.demo.user.impl.jpa.User;
import ru.demo.user.model.Role;
import ru.demo.user.model.UserCreate;
import ru.demo.user.model.UserDetail;
import ru.demo.user.model.UserShort;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class UserMappers implements UserMapper {

    @Override
    public UserShort toShort(User user) {
        var userShort = new UserShort();
        userShort.setUsername(user.getUsername());
        userShort.setId(user.getId());
        return userShort;
    }

    @Override
    public UserDetail toDetail(User user) {
        var detail = new UserDetail();
        detail.setUsername(user.getUsername());
        detail.setId(user.getId());
        detail.setRoles(user.getAuthorities());
        return detail;
    }

    @Override
    public User fromCreate(UserCreate userCreate) {
        var user = new User();
        user.setEnabled(true);
        user.setAuthorities(List.of(Role.USER));
        user.setUsername(userCreate.getUsername());
        user.setPassword("{noop}" + userCreate.getPassword());
        return user;
    }
}