package ru.sergey.patseev.authorization_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sergey.patseev.authorization_service.model.UserEntity;

import java.util.Optional;

/**
 * Repository interface for managing UserEntity objects in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	/**
	 * Retrieves a UserEntity by its username.
	 *
	 * @param username The username of the user.
	 * @return An Optional containing the UserEntity with the specified username, or empty if not found.
	 */
	Optional<UserEntity> findByUsername(String username);

	/**
	 * Retrieves a UserEntity by its email.
	 *
	 * @param email The email of the user.
	 * @return An Optional containing the UserEntity with the specified email, or empty if not found.
	 */
	Optional<UserEntity> findByEmail(String email);

	/**
	 * Retrieves a UserEntity by its activationCode.
	 *
	 * @param activationCode The activationCode of the user.
	 * @return An Optional containing the UserEntity with the specified activationCode, or empty if not found.
	 */
	Optional<UserEntity> findByActivationCode(String activationCode);
}
