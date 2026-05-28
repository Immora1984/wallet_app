package ru.demo.notification.impl;

import org.springframework.stereotype.Component;
import ru.demo.notification.NotificationMapper;
import ru.demo.notification.impl.jpa.Notification;
import ru.demo.notification.model.NotificationCreate;

import java.util.Map;
import java.util.function.BiFunction;

@Component
public class NotificationMappers implements NotificationMapper {

    @Override
    public Notification fromCreate(NotificationCreate model, BiFunction<String, Map<String, Object>, String> render) {
        var notification = new Notification();
        notification.setBody(render.apply(model.getTemplate(), model.getAttributes()));
        notification.setType(model.getType());
        notification.setRecipient(model.getRecipient());
        notification.setHeader(model.getHeader());
        return notification;
    }
}
