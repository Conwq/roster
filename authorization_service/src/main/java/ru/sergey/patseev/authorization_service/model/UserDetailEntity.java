package ru.sergey.patseev.authorization_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Represents the detailed information of a user in the application.
 */
@Entity
@Table(schema = "roster", name = "users_details")
@Data
public class UserDetailEntity {
	/**
	 * The unique identifier of the user.
	 */
	@Id
	@Column(name = "user_id")
	private Long userId;

	/**
	 * The timestamp representing the registration time of the user.
	 */
	@Column(name = "registration_at")
	private Timestamp registrationAt;

	/**
	 * The timestamp representing the last login time of the user.
	 */
	@Column(name = "last_login_at")
	private Timestamp lastLoginAt;

	/**
	 * The reference to the user entity associated with this user detail.
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", table = "users", referencedColumnName = "user_id")
	private UserEntity userEntity; //todo Add Javadoc explaining what this field represents.

	/**
	 * Sets the registration time of the user detail entity to the current timestamp before it is persisted.
	 */
	@PrePersist
	public void setRegistrationTime() {
		registrationAt = Timestamp.from(Instant.now());
	}
}
