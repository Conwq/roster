package ru.sergey.patseev.authorization_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sergey.patseev.authorization_service.model.UserDetailEntity;

/**
 * Repository interface for managing user detail entities.
 */
@Repository
public interface UserDetailRepository extends JpaRepository<UserDetailEntity, Long> {
}

