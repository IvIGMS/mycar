package com.mycar.business.controllers.mappers;

import com.mycar.business.controllers.dto.user.UserRegisterDTO;
import com.mycar.business.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserUserRegisterDTOMapper {
    UserRegisterDTO userEntityToUserResgisterDTO(UserEntity user);
    UserEntity userRegisterDTOToUserEntity(UserRegisterDTO userDTO);
}
