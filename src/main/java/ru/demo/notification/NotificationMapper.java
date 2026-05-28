package ru.demo.notification;

import ru.demo.notification.impl.jpa.Notification;
import ru.demo.notification.model.NotificationCreate;

import java.util.Map;
import java.util.function.BiFunction;

public interface NotificationMapper {

    Notification fromCreate(NotificationCreate model, BiFunction<String, Map<String, Object>, String> render);
}
