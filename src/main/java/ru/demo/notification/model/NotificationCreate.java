package ru.demo.notification.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class NotificationCreate {
    @NotNull
    private NotificationType type;
    @NotBlank
    private String template;
    private Map<String, Object> attributes;
    @NotBlank
    private String recipient;
    private String header;
}