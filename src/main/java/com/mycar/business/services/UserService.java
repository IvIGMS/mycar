package com.mycar.business.services;

import com.mycar.business.controllers.dto.UserDTO;
import com.mycar.business.controllers.dto.UserRegisterDTO;
import com.mycar.business.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public UserDTO createUser(UserRegisterDTO userRegisterDTO);

    UserDTO getUserById(Long id);

    String activateUser(Long id);
}
