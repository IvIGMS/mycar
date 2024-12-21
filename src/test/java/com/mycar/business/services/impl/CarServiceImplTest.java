package com.mycar.business.services.impl;

import com.mycar.business.controllers.dto.car.CarAddKm;
import com.mycar.business.controllers.dto.car.CarCreateDTO;
import com.mycar.business.controllers.dto.car.CarQueryDTO;
import com.mycar.business.controllers.mappers.CarCarQueryDTOMapper;
import com.mycar.business.controllers.mappers.CarCarQueryDTOMapperImpl;
import com.mycar.business.entities.CarEntity;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    private UserEntity user;
    CarQueryDTO car1 = new CarQueryDTO();
    CarEntity carEntity = new CarEntity();
    CarAddKm carAddKm = new CarAddKm();

    private CarCarQueryDTOMapper carCarQueryDTOMapper;
    private CarServiceImpl carService;

    @Mock
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("test_user");

        car1.setId(1L);
        car1.setKm(20000);
        car1.setActive(true);
        car1.setCompanyName("Seat");
        car1.setModelName("Leon");
        car1.setUserId(1L);

        carEntity.setId(1L);
        carEntity.setKm(20000);
        carEntity.setIsActive(true);
        carEntity.setCompanyName("Seat");
        carEntity.setModelName("Leon");
        carEntity.setUserEntity(user);

        carAddKm.setCarId(1L);
        carAddKm.setKm(30000);

        carCarQueryDTOMapper = new CarCarQueryDTOMapperImpl();
        carService = new CarServiceImpl(carRepository, carCarQueryDTOMapper);
    }

    @Test
    void createCar_ok() {
        CarCreateDTO carCreateDTO = CarCreateDTO.builder()
                .companyName("Seat")
                .modelName("Leon")
                .km(20000)
                .build();

        Mockito.when(carRepository.save(Mockito.any())).thenReturn(carEntity);

        CarQueryDTO results = carService.createCar(user, carCreateDTO);

        assertNotNull(results);
        assertEquals("Seat", results.getCompanyName());
        assertEquals("Leon", results.getModelName());
    }

    @Test
    void createCar_ko() {
        CarCreateDTO carCreateDTO = CarCreateDTO.builder()
                .companyName("Seat")
                .modelName("Leon")
                .km(20000)
                .build();

        Mockito.when(carRepository.save(Mockito.any())).thenThrow(new DataIntegrityViolationException("Llave duplicada"));

        CarQueryDTO results = carService.createCar(user, carCreateDTO);

        assertNull(results);
    }

    @Test
    void createCar_ko_numberOfCarsGT5() {
        CarCreateDTO carCreateDTO = CarCreateDTO.builder()
                .companyName("Seat")
                .modelName("Leon")
                .km(20000)
                .build();

        Mockito.when(carRepository.numberOfCarsByUser(Mockito.anyLong())).thenReturn(5);

        CarQueryDTO results = carService.createCar(user, carCreateDTO);

        assertNull(results);
    }

    @Test
    void deactivateCar_ok() {
        Mockito.when(carRepository.existsByUserIdAndCarId(user.getId(), car1.getId())).thenReturn(true);
        Mockito.when(carRepository.deactivateCar(car1.getId())).thenReturn(1);
        Mockito.when(carRepository.isActive(Mockito.anyLong())).thenReturn(true);

        Map<String, String> results = carService.deactivateCar(user, car1.getId());

        assertEquals(1, results.size());
        assertTrue(results.containsKey("OK"));
        assertEquals("El vehículo con id 1 ha sido desactivado correctamente", results.get("OK"));
    }

    @Test
    void deactivateCar_koByExistCarByUsername() {
        Mockito.when(carRepository.existsByUserIdAndCarId(user.getId(), car1.getId())).thenReturn(false);

        Map<String, String> results = carService.deactivateCar(user, car1.getId());

        assertEquals(1, results.size());
        assertTrue(results.containsKey("KO"));
        assertEquals("El vehículo con id 1 no ha podido desactivarse.", results.get("KO"));
    }

    @Test
    void deactivateCar_koIsActiveEqualsFalse() {
        Mockito.when(carRepository.existsByUserIdAndCarId(user.getId(), car1.getId())).thenReturn(true);
        Mockito.when(carRepository.isActive(Mockito.anyLong())).thenReturn(false);

        Map<String, String> results = carService.deactivateCar(user, car1.getId());

        assertEquals(1, results.size());
        assertTrue(results.containsKey("KO"));
        assertEquals("El vehículo con id 1 no ha podido desactivarse.", results.get("KO"));
    }

    @Test
    void deactivateCar_koExistsByUserIdAndCarIdFalse() {
        Mockito.when(carRepository.existsByUserIdAndCarId(user.getId(), car1.getId())).thenReturn(false);

        Map<String, String> results = carService.deactivateCar(user, car1.getId());

        assertEquals(1, results.size());
        assertTrue(results.containsKey("KO"));
        assertEquals("El vehículo con id 1 no ha podido desactivarse.", results.get("KO"));
    }

    @Test
    void deactivateCar_koByDeactivateCarRowsEqualsZero() {
        Mockito.when(carRepository.existsByUserIdAndCarId(user.getId(), car1.getId())).thenReturn(true);
        Mockito.when(carRepository.isActive(Mockito.anyLong())).thenReturn(true);
        Mockito.when(carRepository.deactivateCar(car1.getId())).thenReturn(0);

        Map<String, String> results = carService.deactivateCar(user, car1.getId());

        assertEquals(1, results.size());
        assertTrue(results.containsKey("KO"));
        assertEquals("El vehículo con id 1 no ha podido desactivarse.", results.get("KO"));
    }

    @Test
    void activateCar_ok() {
        Mockito.when(carRepository.existsByUserIdAndCarId(user.getId(), car1.getId())).thenReturn(true);
        Mockito.when(carRepository.activateCar(car1.getId())).thenReturn(1);
        Mockito.when(carRepository.isActive(Mockito.anyLong())).thenReturn(false);

        Map<String, String> results = carService.activateCar(user, car1.getId());

        assertEquals(1, results.size());
        assertTrue(results.containsKey("OK"));
        assertEquals("El vehículo con id 1 ha sido activado correctamente", results.get("OK"));
    }

    @Test
    void activateCar_koByExistCarByUsername() {
        Mockito.when(carRepository.existsByUserIdAndCarId(user.getId(), car1.getId())).thenReturn(false);

        Map<String, String> results = carService.activateCar(user, car1.getId());

        assertEquals(1, results.size());
        assertTrue(results.containsKey("KO"));
        assertEquals("El vehículo con id 1 no ha podido activarse.", results.get("KO"));
    }

    @Test
    void activateCar_koIsActiveEqualsFalse() {
        Mockito.when(carRepository.existsByUserIdAndCarId(user.getId(), car1.getId())).thenReturn(true);
        Mockito.when(carRepository.isActive(Mockito.anyLong())).thenReturn(true);

        Map<String, String> results = carService.activateCar(user, car1.getId());

        assertEquals(1, results.size());
        assertTrue(results.containsKey("KO"));
        assertEquals("El vehículo con id 1 no ha podido activarse.", results.get("KO"));
    }

    @Test
    void activateCar_koExistsByUserIdAndCarIdFalse() {
        Mockito.when(carRepository.existsByUserIdAndCarId(user.getId(), car1.getId())).thenReturn(false);

        Map<String, String> results = carService.activateCar(user, car1.getId());

        assertEquals(1, results.size());
        assertTrue(results.containsKey("KO"));
        assertEquals("El vehículo con id 1 no ha podido activarse.", results.get("KO"));
    }

    @Test
    void activateCar_koByDeactivateCarRowsEqualsZero() {
        Mockito.when(carRepository.existsByUserIdAndCarId(user.getId(), car1.getId())).thenReturn(true);
        Mockito.when(carRepository.isActive(Mockito.anyLong())).thenReturn(false);
        Mockito.when(carRepository.activateCar(car1.getId())).thenReturn(0);

        Map<String, String> results = carService.activateCar(user, car1.getId());

        assertEquals(1, results.size());
        assertTrue(results.containsKey("KO"));
        assertEquals("El vehículo con id 1 no ha podido activarse.", results.get("KO"));
    }

    @Test
    void getCarsOk() {
        List<CarQueryDTO> cars = new ArrayList<>();
        cars.add(car1);

        Mockito.when(carRepository.getCars(user.getId())).thenReturn(cars);

        List<CarQueryDTO> results = carService.getCars(1L);

        assertTrue(results.contains(car1));
        assertEquals("Seat", results.get(0).getCompanyName());
        assertEquals("Leon", results.get(0).getModelName());
    }

    @Test
    void getCarIfThisOwnerUser_ok() {
        Mockito.when(carRepository.findCarByCarIdAndUserId(user.getId(), carEntity.getId())).thenReturn(Optional.of(carEntity));

        CarEntity results = carService.getCarIfThisOwnerUser(user.getId(), carEntity.getId());

        assertNotNull(results);
        assertEquals("Seat", results.getCompanyName());
        assertEquals("Leon", results.getModelName());
    }

    @Test
    void getCarIfThisOwnerUser_ko() {
        Mockito.when(carRepository.findCarByCarIdAndUserId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.empty());
        CarEntity results = carService.getCarIfThisOwnerUser(1L, 1L);
        assertNull(results);
    }

    @Test
    void getAddKm_ok() {
        Mockito.when(carRepository.findCarByCarIdAndUserId(user.getId(), carEntity.getId())).thenReturn(Optional.of(carEntity));
        Mockito.when(carRepository.save(carEntity)).thenReturn(carEntity);

        CarQueryDTO results = carService.addKm(user, carAddKm);

        assertNotNull(results);
        assertEquals(30000, results.getKm());
    }

    @Test
    void getAddKm_koCarNull() {
        Mockito.when(carRepository.findCarByCarIdAndUserId(user.getId(), carEntity.getId())).thenReturn(Optional.empty());
        CarQueryDTO results = carService.addKm(user, carAddKm);
        assertNull(results);
    }

    @Test
    void getAddKm_koDataIntegrityViolationException() {
        Mockito.when(carRepository.findCarByCarIdAndUserId(user.getId(), carEntity.getId())).thenReturn(Optional.of(carEntity));
        Mockito.when(carRepository.save(carEntity)).thenThrow(new DataIntegrityViolationException("Some error"));

        CarQueryDTO results = carService.addKm(user, carAddKm);

        assertNull(results);
    }

    @Test
    void getAddKm_koSameKm() {
        Mockito.when(carRepository.findCarByCarIdAndUserId(user.getId(), carEntity.getId())).thenReturn(Optional.of(carEntity));
        carAddKm.setKm(20000);
        CarQueryDTO results = carService.addKm(user, carAddKm);

        assertNull(results);
    }
}










