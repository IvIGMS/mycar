package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.issue.IssueCreateDTO;
import com.mycar.business.controllers.dto.issue.IssueQueryDTO;
import com.mycar.business.controllers.utils.ControllerHelper;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.services.IssueService;
import com.mycar.business.services.impl.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(IssueController.class)
class IssueControllerTest {
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
        public IssueController issueController(AuthService authService, IssueService issueService, ControllerHelper controllerHelper) {
            return new IssueController(authService, issueService, controllerHelper);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private IssueService issueService;

    @MockBean
    private ControllerHelper controllerHelper;

    private UserEntity userEntity;
    private Pageable pageable;
    private Page<IssueQueryDTO> issuesPage;
    private IssueQueryDTO issueQueryDTO;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test_user");
        userEntity.setEmail("inventado@gmail.com");
        userEntity.setFirstName("Usuario");
        userEntity.setLastName("Invitado");

        pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());

        issueQueryDTO = IssueQueryDTO.builder().name("Cambio de ruedas").id(1L).carId(1L).build();

        List<IssueQueryDTO> issueQueryDTOS = new ArrayList<IssueQueryDTO>();
        issueQueryDTOS.add(issueQueryDTO);

        issuesPage = new PageImpl<>(issueQueryDTOS, pageable, 1);
    }

    @Test
    void getIssues() throws Exception {
        Mockito.when(authService.getLoggedInUser(Mockito.any(HttpServletRequest.class))).thenReturn(userEntity);
        Mockito.when(controllerHelper.getPageable(Mockito.any(HttpServletRequest.class))).thenReturn(pageable);
        Mockito.when(issueService.getIssues(Mockito.anyLong(), Mockito.any(Pageable.class))).thenReturn(issuesPage);

        mockMvc.perform(get("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Cambio de ruedas"))
                .andExpect(jsonPath("$.content[0].carId").value(1L))
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @Test
    void create_ok() throws Exception {
        Mockito.when(authService.getLoggedInUser(Mockito.any(HttpServletRequest.class))).thenReturn(userEntity);
        Mockito.when(issueService.createIssue(Mockito.any(UserEntity.class), Mockito.any(IssueCreateDTO.class))).thenReturn(issueQueryDTO);

        mockMvc.perform(post("/api/issues")
                .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "name": "Cambio de ruedas",
                                        "currentDistance": 20000,
                                        "notificationDistance": 50000,
                                        "typeId": 1,
                                        "carId": 1
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cambio de ruedas"))
                .andExpect(jsonPath("$.carId").value(1L))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void create_ko() throws Exception {
        Mockito.when(authService.getLoggedInUser(Mockito.any(HttpServletRequest.class))).thenReturn(userEntity);
        Mockito.when(issueService.createIssue(Mockito.any(UserEntity.class), Mockito.any(IssueCreateDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "name": "Cambio de ruedas",
                                        "currentDistance": 20000,
                                        "notificationDistance": 50000,
                                        "typeId": 1,
                                        "carId": 1
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ha habido un error al guardar el issue en la base de datos"));
    }

    @Test
    void getIssueById_ok() throws Exception{
        Mockito.when(authService.getLoggedInUser(Mockito.any(HttpServletRequest.class))).thenReturn(userEntity);
        Mockito.when(issueService.getIssueById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(issueQueryDTO);

        mockMvc.perform(get("/api/issues/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cambio de ruedas"))
                .andExpect(jsonPath("$.carId").value(1L))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getIssueById_ko() throws Exception{
        Mockito.when(authService.getLoggedInUser(Mockito.any(HttpServletRequest.class))).thenReturn(userEntity);
        Mockito.when(issueService.getIssueById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/issues/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró el Issue con ID: 1. Es probable que pertenezca a otro usuario."));
    }
}