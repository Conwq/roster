package ru.sergey.patseev.authorization_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a role entity in the application.
 */
@Entity
@Table(schema = "roster", name = "roles")
@Data
public class RoleEntity {

	/**
	 * The unique identifier of the role.
	 */
	@Id
	@Column(name = "role_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roster.role_id_sequence")
	private Long roleId;

	/**
	 * The name of the role.
	 */
	@Column(name = "role_name")
	private String roleName;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<UserEntity> users = new HashSet<>();

	/**
	 * Generates a hash code for this role entity.
	 *
	 * @return the hash code value for this role entity
	 */
	@Override
	public int hashCode() {
		return Objects.hash(roleId, roleName);
	}
}
