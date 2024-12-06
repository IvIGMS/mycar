package com.mycar.business.controllers.mappers;

import com.mycar.business.controllers.dto.user.UserDTO;
import com.mycar.business.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserUserDTOMapper {
    UserDTO userEntityToUserDTO(UserEntity user);
    UserEntity userDTOToUserEntity(UserDTO userDTO);
}
