package ward.peter.olp_courses;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCourseNotification(String message) {
        kafkaTemplate.send("course-notifications", message);
        System.out.println("Sent message: " + message);
    }
}
