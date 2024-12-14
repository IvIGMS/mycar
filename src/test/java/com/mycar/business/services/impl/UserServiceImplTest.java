package com.mycar.business.services.impl;

import com.mycar.business.controllers.dto.user.UserDTO;
import com.mycar.business.controllers.dto.user.UserRegisterDTO;
import com.mycar.business.controllers.mappers.UserUserDTOMapper;
import com.mycar.business.controllers.mappers.UserUserDTOMapperImpl;
import com.mycar.business.controllers.mappers.UserUserRegisterDTOMapper;
import com.mycar.business.controllers.mappers.UserUserRegisterDTOMapperImpl;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserUserRegisterDTOMapper userUserRegisterDTOMapper;
    private UserUserDTOMapper userUserDTOMapper;
    private UserEntity userEntity;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userUserRegisterDTOMapper = new UserUserRegisterDTOMapperImpl();
        userUserDTOMapper = new UserUserDTOMapperImpl();
        userService = new UserServiceImpl(userUserRegisterDTOMapper, userUserDTOMapper, userRepository, passwordEncoder);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test_user");
        userEntity.setEmail("inventado@gmail.com");
        userEntity.setFirstName("Usuario");
        userEntity.setLastName("Invitado");

    }

    @Test
    void createUser_ok() {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder()
                .email("inventado@gmail.com").firstName("Usuario").lastName("Invitado").username("test_user")
                .build();

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(userEntity);
        Mockito.when(passwordEncoder.encode(userRegisterDTO.getPassword())).thenReturn("4nDNdsl32nL/.AJn2L");
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(userEntity);

        UserDTO results = userService.createUser(userRegisterDTO);

        assertNotNull(results);
        assertEquals(userRegisterDTO.getUsername(), results.getUsername());
        assertEquals(userRegisterDTO.getEmail(), results.getEmail());
    }

    @Test
    void getUserById_ok() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userEntity));

        UserDTO results = userService.getUserById(Mockito.anyLong());

        assertNotNull(results);
        assertEquals("Usuario", results.getFirstName());
        assertEquals("inventado@gmail.com", results.getEmail());
    }

    @Test
    void getUserById_ko() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        UserDTO results = userService.getUserById(Mockito.anyLong());

        assertNotNull(results);
        assertNull(results.getUsername());
        assertNull(results.getEmail());
    }

    @Test
    void activateUser_ok() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userEntity));
        String results = userService.activateUser(1L);
        assertEquals("El usuario con id 1 ha sido activado", results);
    }

    @Test
    void activateUser_ko() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        String results = userService.activateUser(1L);
        assertEquals("No se ha podido activar el usuario", results);
    }
}