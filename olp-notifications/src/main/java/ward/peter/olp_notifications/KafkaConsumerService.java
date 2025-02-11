package ward.peter.olp_notifications;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "course-notifications", groupId = "notification-group")
    public void consumeMessage(String message) {
        System.out.println("📢 New Notification: " + message);
        // You could also send emails or push notifications here
    }
}
