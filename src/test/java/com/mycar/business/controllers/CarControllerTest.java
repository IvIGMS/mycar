package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.car.CarAddKm;
import com.mycar.business.controllers.dto.car.CarCreateDTO;
import com.mycar.business.controllers.dto.car.CarQueryDTO;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.services.CarService;
import com.mycar.business.services.impl.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebMvcTest(CarController.class)
class CarControllerTest {
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
        public CarController carController(CarService carService, AuthService authService) {
            return new CarController(carService, authService);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private CarService carService;

    private UserEntity userEntity;
    private CarQueryDTO carQueryDTOOne;
    private CarQueryDTO carQueryDTOTwo;
    private List<CarQueryDTO> carQueryDTOList;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test_user");
        userEntity.setEmail("inventado@gmail.com");
        userEntity.setFirstName("Usuario");
        userEntity.setLastName("Invitado");

        carQueryDTOOne = new CarQueryDTO();
        carQueryDTOOne.setId(1L);
        carQueryDTOOne.setKm(20000);
        carQueryDTOOne.setActive(true);
        carQueryDTOOne.setCompanyName("Seat");
        carQueryDTOOne.setModelName("Leon");
        carQueryDTOOne.setUserId(1L);

        carQueryDTOTwo = new CarQueryDTO();
        carQueryDTOTwo.setId(2L);
        carQueryDTOTwo.setKm(60000);
        carQueryDTOTwo.setActive(true);
        carQueryDTOTwo.setCompanyName("Toyota");
        carQueryDTOTwo.setModelName("Auris");
        carQueryDTOTwo.setUserId(1L);

        carQueryDTOList = new ArrayList<>();
        carQueryDTOList.add(carQueryDTOOne);
        carQueryDTOList.add(carQueryDTOTwo);
    }
    @Test
    void create_ok() throws Exception {
        Mockito.when(authService.getLoggedInUser(Mockito.any())).thenReturn(userEntity);
        Mockito.when(carService.createCar(Mockito.any(UserEntity.class), Mockito.any(CarCreateDTO.class))).thenReturn(carQueryDTOOne);

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "companyName": "Seat",
                                    "modelName": "Leon",
                                    "km": 20000
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.companyName").value("Seat"))
                .andExpect(jsonPath("$.modelName").value("Leon"))
                .andExpect(jsonPath("$.km").value(20000));
    }

    @Test
    void create_ko() throws Exception {
        Mockito.when(authService.getLoggedInUser(Mockito.any())).thenReturn(userEntity);
        Mockito.when(carService.createCar(Mockito.any(UserEntity.class), Mockito.any(CarCreateDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "companyName": "Seat",
                                    "modelName": "Leon",
                                    "km": 20000
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ha habido un error al guardar el vehículo en la base de datos"));
    }

    @Test
    void deactivateCar_ok() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("OK", "El vehículo con id 1 ha sido desactivado correctamente");

        Mockito.when(authService.getLoggedInUser(Mockito.any())).thenReturn(userEntity);
        Mockito.when(carService.deactivateCar(Mockito.any(UserEntity.class), Mockito.any(Long.class))).thenReturn(map);

        mockMvc.perform(patch("/api/cars/deactivate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.OK").value("El vehículo con id 1 ha sido desactivado correctamente"));
    }

    @Test
    void deactivateCar_ko() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("KO", "El vehículo con id 1 no ha podido desactivarse.");

        Mockito.when(authService.getLoggedInUser(Mockito.any())).thenReturn(userEntity);
        Mockito.when(carService.deactivateCar(Mockito.any(UserEntity.class), Mockito.any(Long.class))).thenReturn(map);

        mockMvc.perform(patch("/api/cars/deactivate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.KO").value("El vehículo con id 1 no ha podido desactivarse."));
    }

    @Test
    void getCars() throws Exception{
        Mockito.when(authService.getLoggedInUser(Mockito.any())).thenReturn(userEntity);
        Mockito.when(carService.getCars(Mockito.anyLong())).thenReturn(carQueryDTOList);

        mockMvc.perform(get("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].companyName").value("Seat"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].companyName").value("Toyota"));
    }

    @Test
    void addKm_ok() throws Exception {
        carQueryDTOOne.setKm(90000);

        Mockito.when(authService.getLoggedInUser(Mockito.any())).thenReturn(userEntity);
        Mockito.when(carService.addKm(Mockito.any(UserEntity.class), Mockito.any(CarAddKm.class))).thenReturn(carQueryDTOOne);

        mockMvc.perform(patch("/api/cars/addKm")
                .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"carId": 1, "km": 90000}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.companyName").value("Seat"))
                .andExpect(jsonPath("$.modelName").value("Leon"))
                .andExpect(jsonPath("$.km").value(90000));
    }

    @Test
    void addKm_ko() throws Exception {
        Mockito.when(authService.getLoggedInUser(Mockito.any())).thenReturn(userEntity);
        Mockito.when(carService.addKm(Mockito.any(UserEntity.class), Mockito.any(CarAddKm.class))).thenReturn(null);

        mockMvc.perform(patch("/api/cars/addKm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"carId": 3, "km": 90000}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ha habido un error al actualizar los km del vehículo"));
    }

    @Test
    void activateCar_ok() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("OK", "El vehículo con id 1 ha sido activado correctamente");

        Mockito.when(authService.getLoggedInUser(Mockito.any())).thenReturn(userEntity);
        Mockito.when(carService.activateCar(Mockito.any(UserEntity.class), Mockito.any(Long.class))).thenReturn(map);

        mockMvc.perform(patch("/api/cars/activate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.OK").value("El vehículo con id 1 ha sido activado correctamente"));
    }

    @Test
    void activateCar_ko() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("KO", "El vehículo con id 1 no ha podido activarse.");

        Mockito.when(authService.getLoggedInUser(Mockito.any())).thenReturn(userEntity);
        Mockito.when(carService.activateCar(Mockito.any(UserEntity.class), Mockito.any(Long.class))).thenReturn(map);

        mockMvc.perform(patch("/api/cars/activate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.KO").value("El vehículo con id 1 no ha podido activarse."));
    }
}