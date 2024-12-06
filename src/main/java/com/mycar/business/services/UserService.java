package com.mycar.business.services;

import com.mycar.business.controllers.dto.user.UserDTO;
import com.mycar.business.controllers.dto.user.UserRegisterDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public UserDTO createUser(UserRegisterDTO userRegisterDTO);

    UserDTO getUserById(Long id);

    String activateUser(Long id);
}
