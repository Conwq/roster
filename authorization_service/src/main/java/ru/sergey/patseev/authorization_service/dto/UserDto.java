package ru.sergey.patseev.authorization_service.dto;

/**
 * Data transfer object representing a user.
 */
public record UserDto(Long userId, String email, String username, String password) {
}
