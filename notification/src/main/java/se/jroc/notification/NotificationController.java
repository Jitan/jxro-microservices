package se.jroc.notification;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import se.jroc.clients.notification.NotificationRequest;

@Slf4j
@RestController
@RequestMapping("api/v1/notification")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public void registerNotification(@RequestBody NotificationRequest request) {
        log.info("Notification Request received: {}", request);
        notificationService.send(request);
    }
}