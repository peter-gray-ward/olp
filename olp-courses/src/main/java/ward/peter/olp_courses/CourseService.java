package ward.peter.olp_courses;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "courses") // ✅ Cache data when calling findAllCourses()
    public List<Course> findAllCourses() {
        System.out.println("Fetching courses from database...");
        return repository.findAll();
    }

    @CacheEvict(value = "courses", allEntries = true) // ✅ Clears cache when adding a course
    public Course addCourse(Course course) {
        return repository.save(course);
    }

    @CacheEvict(value = "courses", allEntries = true) // ✅ Clears cache when adding multiple courses
    public List<Course> saveAllCourses(List<Course> courses) {
        return repository.saveAll(courses);
    }

    @CacheEvict(value = "courses", allEntries = true) // ✅ Clears cache when deleting
    public void deleteCourse(Long id) {
        repository.deleteById(id);
    }
}
