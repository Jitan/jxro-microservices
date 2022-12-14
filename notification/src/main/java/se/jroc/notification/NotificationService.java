package se.jroc.notification;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.jroc.clients.notification.NotificationRequest;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest request) {
        Notification notification = Notification.builder()
                .toCustomerId(request.toCustomerId())
                .toCustomerEmail(request.toCustomerEmail())
                .message(request.message())
                .sentAt(LocalDateTime.now())
                .sender("MonkeyRo")
                .build();
        notificationRepository.saveAndFlush(notification);
    }
}
