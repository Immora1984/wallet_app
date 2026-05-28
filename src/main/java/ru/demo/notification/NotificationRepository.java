package ru.demo.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.demo.notification.impl.jpa.Notification;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

}
