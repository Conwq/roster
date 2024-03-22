package ru.sergey.patseev.authorization_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a user entity in the application.
 */
@Entity
@Table(schema = "roster", name = "users")
@Data
public class UserEntity {

	/**
	 * The unique identifier of the user.
	 */
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	/**
	 * The email address of the user.
	 */
	private String email;

	/**
	 * The username of the user.
	 */
	private String username;

	/**
	 * The password of the user.
	 */
	private String password;

	/**
	 * Indicates whether the user is activated or not.
	 */
	private boolean isActivated;

	/**
	 * The activation code for the user.
	 */
	private String activationCode;

	/**
	 * The roles associated with the user.
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			schema = "roster",
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<RoleEntity> roles = new HashSet<>();

	/**
	 * The detail information of the user.
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userEntity")
	private UserDetailEntity userDetailEntity;

	/**
	 * Calculates the hash code of the user entity based on its fields.
	 *
	 * @return The hash code value of the user entity.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(userId, username, password);
	}
}
