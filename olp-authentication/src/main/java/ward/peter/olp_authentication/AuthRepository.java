package ward.peter.olp_authentication;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ward.peter.olp_authentication.dto.User;

public interface AuthRepository extends JpaRepository<User, Integer> {
	public Optional<User> findByUsername(String username);
}