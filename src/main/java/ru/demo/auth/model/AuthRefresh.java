package ru.demo.auth.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class AuthRefresh {
    private UUID refreshToken;
}
