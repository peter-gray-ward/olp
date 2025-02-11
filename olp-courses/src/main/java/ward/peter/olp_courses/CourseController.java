package ward.peter.olp_courses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
	private final CourseService service;
	private final KafkaProducerService kafka;

	public CourseController(CourseService service, KafkaProducerService kafka) {
		this.service = service;
		this.kafka = kafka;
	}

	@GetMapping
	public ResponseEntity<List<Course>> findAllCourses() {
		return ResponseEntity.ok(service.findAllCourses());
	}

	@PostMapping
	public ResponseEntity<List<Course>> saveAllCourses(@RequestBody List<Course> courses) {
		List<Course> newCourses = service.saveAllCourses(courses);
		kafka.sendCourseNotification("New courses created: " + newCourses
			.stream()
			.map(Course::getTitle)
			.collect(Collectors.joining(",\n"))
		);
		return ResponseEntity.ok(newCourses);
	}
}