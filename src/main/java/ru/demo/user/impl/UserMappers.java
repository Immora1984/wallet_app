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

    User fromCreate(UserCreate userCreate) {
        var user = new User();
        user.setAuthorities(List.of(Role.USER));
        user.setPassword("{noop}" + userCreate.getPassword());
        user.setUsername(userCreate.getEmail());
        user.setEnabled(true);
        return user;
    }

    void update(User user, OAuth2User oAuth2User, boolean enabled) {
        user.setUsername(oAuth2User.getName());
        if (user.getAuthorities() == null) {
            user.setAuthorities(List.of(Role.RENTER));
            user.setEnabled(enabled);
        }
    }

    @Override
    public UserShort toShort(User user) {
        var userShort = new UserShort();
        userShort.setFirstName(user.getFirstName());
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
}