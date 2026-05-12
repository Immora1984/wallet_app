package ru.demo.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.demo.user.UserMapper;
import ru.demo.user.impl.jpa.User;
import ru.demo.user.model.UserDetail;
import ru.demo.user.model.UserShort;

@Slf4j
@Component
@RequiredArgsConstructor
class UserMappers implements UserMapper {

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