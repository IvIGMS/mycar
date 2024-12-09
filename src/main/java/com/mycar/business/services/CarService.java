package com.mycar.business.services;

import com.mycar.business.controllers.dto.car.CarCreateDTO;
import com.mycar.business.controllers.dto.car.CarQueryDTO;
import com.mycar.business.entities.CarEntity;
import com.mycar.business.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CarService {
    CarQueryDTO createCar(UserEntity user, CarCreateDTO carCreateDTO);

    Map<String, String> deactivateCar(UserEntity user, Long carId);
    List<CarQueryDTO> getCars(Long userId);

    CarEntity getCarIfThisOwnerUser(Long userId, Long carId);
}