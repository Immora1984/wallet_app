package ru.demo.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreate {
    @NotNull
    @NotBlank
    @Schema(description = "Юзернейм")
    private String username;
    @NotNull
    @NotBlank
    @Schema(description = "Пароль")
    private String password;
}
