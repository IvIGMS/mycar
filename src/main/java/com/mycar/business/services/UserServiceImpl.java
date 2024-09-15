package com.mycar.business.services;

import com.mycar.business.controllers.dto.UserDTO;
import com.mycar.business.controllers.dto.UserRegisterDTO;
import com.mycar.business.controllers.mappers.UserUserDTOMapper;
import com.mycar.business.controllers.mappers.UserUserRegisterDTOMapper;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserUserRegisterDTOMapper userUserRegisterDTOMapper;
    @Autowired
    UserUserDTOMapper userUserDTOMapper;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {
        UserEntity user = userUserRegisterDTOMapper.userRegisterDTOToUserEntity(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
