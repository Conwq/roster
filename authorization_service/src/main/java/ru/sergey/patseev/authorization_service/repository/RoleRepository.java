package ru.sergey.patseev.authorization_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sergey.patseev.authorization_service.model.RoleEntity;

/**
 * Repository interface for managing RoleEntity objects in the database.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

	/**
	 * Retrieves a RoleEntity by its role name.
	 *
	 * @param roleName The name of the role.
	 * @return The RoleEntity with the specified role name, or null if not found.
	 */
	RoleEntity getRoleEntityByRoleName(String roleName);
}