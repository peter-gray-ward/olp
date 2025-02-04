package ward.peter.olp_courses;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

import java.util.List;

@Service
public class CourseService {
	public final CourseRepository repository;

	public CourseService(CourseRepository repository) {
		this.repository = repository;
	}

	public List<Course> findAllCourses() {
		return repository.findAll();
	}
}