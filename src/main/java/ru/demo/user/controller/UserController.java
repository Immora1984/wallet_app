package ru.demo.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.demo.user.UserService;
import ru.demo.user.model.*;
import ru.demo.user.model.UserModify.UserUpdate;
import ru.demo.util.ApiOperation;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(
            path = "/{id}",
            method = RequestMethod.GET,
            tags = "Пользователи"
    )
    UserDetail userById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @ApiOperation(
            path = "/info",
            method = RequestMethod.GET,
            tags = "Пользователи"
    )
    UserDetail userInfo(@AuthenticationPrincipal UUID userId) {
        return userService.getById(userId);
    }

    @ApiOperation(
            path = "/{id}",
            method = RequestMethod.DELETE,
            tags = "Пользователи",
            authorize = "hasAuthority('ADMIN') or (#id == principal)"
    )
    void userDelete(@PathVariable UUID id) {
        userService.deleteUser(id, false);
    }


    @ApiOperation(
            path = "/{userId}",
            method = RequestMethod.PUT,
            tags = "Пользователи"
    )
    void updateUser(@PathVariable UUID userId, @RequestBody UserUpdate request) {
        userService.userUpdate(userId, request);
    }

    @ApiOperation(
            method = RequestMethod.POST,
            authorize = "permitAll()",
            tags = "Пользователи"
    )
    void registerUser(@Valid @RequestBody UserCreate userCreate) {
        userService.registerUser(userCreate);
    }
}