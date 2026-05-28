package ru.demo.notification;

import ru.demo.notification.model.NotificationCreate;

public interface NotificationService {

    void sendAsync(NotificationCreate model);

}
