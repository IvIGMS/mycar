package com.mycar.business.services.impl;

import com.mycar.business.controllers.dto.user.UserDTO;
import com.mycar.business.controllers.dto.user.UserRegisterDTO;
import com.mycar.business.controllers.mappers.UserUserDTOMapper;
import com.mycar.business.controllers.mappers.UserUserRegisterDTOMapper;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.repositories.UserRepository;
import com.mycar.business.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserUserRegisterDTOMapper userUserRegisterDTOMapper;
    private final UserUserDTOMapper userUserDTOMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserUserRegisterDTOMapper userUserRegisterDTOMapper, UserUserDTOMapper userUserDTOMapper,
            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userUserRegisterDTOMapper = userUserRegisterDTOMapper;
        this.userUserDTOMapper = userUserDTOMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {
        // Fixme: it's probably to thow an exception, make a try catch.
        UserEntity user = userUserRegisterDTOMapper.userRegisterDTOToUserEntity(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Fixme: in production this is false.
        user.setIsActive(true);
        UserEntity userSaved = userRepository.save(user);
        return userUserDTOMapper.userEntityToUserDTO(userSaved);
    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isPresent()){
            return userUserDTOMapper.userEntityToUserDTO(userEntity.get());
        } else {
            return UserDTO.builder().build();
        }
    }

    @Override
    public String activateUser(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isPresent()){
            userRepository.activateUser(id);
            return "El usuario con id " + id + " ha sido activado";
        } else {
            return "No se ha podido activar el usuario";
        }
    }
}
