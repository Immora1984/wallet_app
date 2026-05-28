package ru.demo.notification.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.demo.notification.NotificationMapper;
import ru.demo.notification.NotificationRepository;
import ru.demo.notification.NotificationService;
import ru.demo.notification.impl.jpa.Notification;
import ru.demo.notification.model.NotificationCreate;
import ru.demo.notification.model.NotificationStatus;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final Configuration ftlFactory;
    private final JavaMailSender mailSender;

    @Async
    @Override
    @Transactional
    public void sendAsync(NotificationCreate model) {
        var notification = notificationMapper.fromCreate(model, this::render);
        try {
            switch (notification.getType()) {
                case EMAIL -> sendEmail(notification);
                case SMS, PUSH -> {}
            }
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.ERROR);
        }
        notification.setStatus(NotificationStatus.DELIVERED);

        notificationRepository.save(notification);
    }

    public void sendEmail(Notification notification) {
        if (mailSender instanceof JavaMailSenderImpl jms && jms.getUsername() != null) {
            var message = mailSender.createMimeMessage();

            try {
                var helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setText(notification.getBody(), true);
                helper.setSubject(notification.getHeader());
                helper.setTo(notification.getRecipient());
                helper.setFrom(jms.getUsername());
            } catch (MessagingException e) {
                log.error(e.getLocalizedMessage());
            }
            mailSender.send(message);
        }
    }

    String render(String template, Map<String, Object> attributes) {
        try (var sw = new StringWriter()) {
            ftlFactory.getTemplate(template).process(attributes, sw);
            return sw.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
