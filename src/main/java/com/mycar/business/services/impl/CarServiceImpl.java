package com.mycar.business.services.impl;

import com.mycar.business.controllers.dto.car.CarAddKm;
import com.mycar.business.controllers.dto.car.CarCreateDTO;
import com.mycar.business.controllers.dto.car.CarQueryDTO;
import com.mycar.business.controllers.mappers.CarCarQueryDTOMapper;
import com.mycar.business.entities.CarEntity;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.repositories.CarRepository;
import com.mycar.business.services.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarCarQueryDTOMapper carCarQueryDTOMapper;

    public CarServiceImpl(CarRepository carRepository, CarCarQueryDTOMapper carCarQueryDTOMapper) {
        this.carRepository = carRepository;
        this.carCarQueryDTOMapper = carCarQueryDTOMapper;
    }

    @Override
    public CarQueryDTO createCar(UserEntity user, CarCreateDTO carCreateDTO) {
        CarEntity carSaved = null;
        CarEntity car = CarEntity.builder()
                .companyName(carCreateDTO.getCompanyName())
                .modelName(carCreateDTO.getModelName())
                .km(carCreateDTO.getKm())
                .userEntity(user)
                .isActive(true)
                .build();
        try{
            carSaved = carRepository.save(car);
            log.info("Vehículo guardado correctamente");
        } catch (DataIntegrityViolationException e){
            log.error("El vehículo no ha podido guardarse, tiene la llave duplicada");
        }
        return carCarQueryDTOMapper.carEntityToCarQueryDTO(carSaved);
    }

    @Override
    public Map<String, String> deactivateCar(UserEntity user, Long carId) {
        Map<String, String> results = new HashMap<>();;
        if(existCarByUsername(user.getId(), carId)){
            Integer affectedRows = carRepository.deactivateCar(carId);
            if(affectedRows.equals(1)){
                results.put("OK", "El vehículo con id " + carId + " ha sido descativado correctamente");
            } else {
                results.put("KO", "El vehículo con id " + carId + " no ha podido desactivarse.");
            }
        } else {
            results.put("KO", "El vehículo con id " + carId + " no ha podido desactivarse.");
        }
        return results;
    }

    @Override
    public List<CarQueryDTO> getCars(Long userId) {
        return carRepository.getCars(userId);
    }

    @Override
    public CarEntity getCarIfThisOwnerUser(Long userId, Long carId) {
        return carRepository.findCarByCarIdAndUserId(userId, carId).orElse(null);
    }

    @Override
    public CarQueryDTO addKm(UserEntity user, CarAddKm carAddKm) {
        CarEntity car = getCarIfThisOwnerUser(user.getId(), carAddKm.getCarId());
        if(car!=null){
            car.setKm(carAddKm.getKm());
            try{
                CarEntity savedCar = carRepository.save(car);
                log.info("Se han actualizado los km del vehículo");
                return carCarQueryDTOMapper.carEntityToCarQueryDTO(savedCar);
            } catch(DataIntegrityViolationException e){
                log.error("No han podido actualizarse los km del vehículo");
                return null;
            }
        }
        log.error("No han podido actualizarse los km del vehículo porque no existe o no pertenece a este usuario.");
        return null;
    }

    private boolean existCarByUsername(Long userId, Long carId) {
        return carRepository.existsByUserIdAndCarIdAndIsActive(userId, carId);
    }
}