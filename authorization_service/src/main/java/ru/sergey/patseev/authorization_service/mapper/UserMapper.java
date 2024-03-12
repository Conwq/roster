package ru.sergey.patseev.authorization_service.mapper;

import org.mapstruct.Mapper;
import ru.sergey.patseev.authorization_service.dto.UserDto;
import ru.sergey.patseev.authorization_service.model.UserEntity;

/**
 * Mapper interface for converting between UserDto and UserEntity objects.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

	/**
	 * Converts a UserDto object to a UserEntity object.
	 *
	 * @param userDto the UserDto object to convert
	 * @return the corresponding UserEntity object
	 */
	UserEntity toEntity(UserDto userDto);
}
