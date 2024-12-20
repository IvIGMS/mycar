package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.issue.IssueQueryDTO;
import com.mycar.business.controllers.dto.user.UserDTO;
import com.mycar.business.controllers.dto.user.UserRegisterDTO;
import com.mycar.business.controllers.utils.ControllerHelper;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.services.IssueService;
import com.mycar.business.services.UserService;
import com.mycar.business.services.impl.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Configuration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeHttpRequests().anyRequest().permitAll();
            return http.build();
        }
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserController userController(UserService userService) {
            return new UserController(userService);
        }
    }

    private UserDTO userDTO;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @BeforeEach
    public void setUp() {
        userDTO = UserDTO.builder().id(1L).username("test_user").email("test_user@mycar.com")
                .firstName("Jhon").lastName("Doe").build();
    }

    @Test
    void createUser() throws Exception {
        Mockito.when(userService.createUser(Mockito.any(UserRegisterDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "test_user",
                                    "email": "test_user@mycar.com",
                                    "password": "password",
                                    "firstName": "Jhon",
                                    "lastName": "Doe"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("test_user"))
                .andExpect(jsonPath("$.email").value("test_user@mycar.com"))
                .andExpect(jsonPath("$.firstName").value("Jhon"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void getUser() throws Exception {
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("test_user"))
                .andExpect(jsonPath("$.email").value("test_user@mycar.com"))
                .andExpect(jsonPath("$.firstName").value("Jhon"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Disabled
    @Test
    void activateUser() throws Exception{

    }
}