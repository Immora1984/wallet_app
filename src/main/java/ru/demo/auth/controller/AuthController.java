package ru.demo.auth.controller;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.demo.auth.AuthService;
import ru.demo.auth.model.AuthRefresh;
import ru.demo.auth.model.AuthToken;
import ru.demo.util.ApiOperation;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @ApiOperation(
            path = "/refresh",
            method = RequestMethod.GET
    )
    ResponseEntity<@NonNull AuthToken> refresh(@CookieValue(name = "Authorization", required = false) UUID refresh,
                                               @RequestBody AuthRefresh request) {
        if (refresh != null) request.setRefreshToken(refresh);
        var token = authService.updateAccessToken(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, "Authorization=" + token.getRefreshToken() + "; HttpOnly")
                .body(token);
    }
}
