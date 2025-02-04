package ward.peter.olp_courses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
	private final CourseService service;

	public CourseController(CourseService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<Course>> findAllCourses() {
		return ResponseEntity.ok(service.findAllCourses());
	}

	@PostMapping
	public ResponseEntity<List<Course>> saveAllCourses(@RequestBody List<Course> courses) {
		return ResponseEntity.ok(service.saveAllCourses(courses));
	}
}