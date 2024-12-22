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
import java.util.Optional;

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

        if(isLessThanFiveCar(user)){
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
        } else {
            log.error("El vehículo no ha podido guardarse, se ha llegado al máximo de 5 coches por usuario.");
        }
        return carCarQueryDTOMapper.carEntityToCarQueryDTO(carSaved);
    }

    private boolean isLessThanFiveCar(UserEntity user) {
        return carRepository.numberOfCarsByUser(user.getId()) < 5;
    }

    @Override
    public Map<String, String> deactivateCar(UserEntity user, Long carId) {
        Map<String, String> results = new HashMap<>();;
        if(existCarByUsername(user.getId(), carId) && isActive(carId)){
            Integer affectedRows = carRepository.deactivateCar(carId);
            if(affectedRows.equals(1)){
                results.put("OK", "El vehículo con id " + carId + " ha sido desactivado correctamente");
            } else {
                results.put("KO", "El vehículo con id " + carId + " no ha podido desactivarse.");
            }
        } else {
            results.put("KO", "El vehículo con id " + carId + " no ha podido desactivarse.");
        }
        return results;
    }

    @Override
    public Map<String, String> activateCar(UserEntity user, Long carId) {
        Map<String, String> results = new HashMap<>();;
        if(existCarByUsername(user.getId(), carId) && !isActive(carId)){
            Integer affectedRows = carRepository.activateCar(carId);
            if(affectedRows.equals(1)){
                results.put("OK", "El vehículo con id " + carId + " ha sido activado correctamente");
            } else {
                results.put("KO", "El vehículo con id " + carId + " no ha podido activarse.");
            }
        } else {
            results.put("KO", "El vehículo con id " + carId + " no ha podido activarse.");
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
            if(!car.getKm().equals(carAddKm.getKm())){
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
            log.error("No han podido actualizarse los km del vehículo porque no se han modificado");
            return null;
        }
        log.error("No han podido actualizarse los km del vehículo porque no existe o no pertenece a este usuario.");
        return null;
    }

    @Override
    public CarQueryDTO getCarById(Long userId, Long carId) {
        if(existCarByUsername(userId, carId)){
            Optional<CarEntity> car = carRepository.findById(carId);
            return carCarQueryDTOMapper.carEntityToCarQueryDTO(car.orElse(null));
        }
        return null;
    }

    @Override
    public CarQueryDTO deleteCarById(Long userId, Long carId) {
        if(existCarByUsername(userId, carId)){
            Optional<CarEntity> foundCar = carRepository.findById(carId);
            if(foundCar.isPresent()){
                carRepository.deleteById(carId);
                Optional<CarEntity> foundCarAfterDelete = carRepository.findById(carId);
                if(foundCarAfterDelete.isEmpty()){
                    return carCarQueryDTOMapper.carEntityToCarQueryDTO(foundCar.get());
                }
                log.error("El vehículo no ha podido ser eliminado. Ha habido un error al eliminarlo en base de datos.");
                return null;
            }
            log.error("El vehículo no ha podido ser eliminado. No ha sido encontrado con este id.");
            return null;
        }
        log.error("El vehículo no ha podido ser eliminado. No existe o no exitste para este usuario.");
        return null;
    }

    private boolean existCarByUsername(Long userId, Long carId) {
        return carRepository.existsByUserIdAndCarId(userId, carId);
    }

    private boolean isActive(Long carId){
        return carRepository.isActive(carId);
    }
}