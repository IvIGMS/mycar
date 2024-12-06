package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.car.CarCreateDTO;
import com.mycar.business.controllers.dto.car.CarQueryDTO;
import com.mycar.business.entities.CarEntity;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.services.CarService;
import com.mycar.business.services.impl.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cars")
@Slf4j
public class CarController {
    @Autowired
    private CarService carService;

    @Autowired
    private AuthService authService;

    @PostMapping()
    public ResponseEntity<Object> create(HttpServletRequest request, @Valid @RequestBody CarCreateDTO carCreateDTO){
        UserEntity user = authService.getLoggedInUser(request);
        CarQueryDTO car = carService.createCar(user, carCreateDTO);

        if (car!=null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(car.getId())
                    .toUri();
            return ResponseEntity.created(location).body(car);
        } else {
            return ResponseEntity.badRequest().body("Ha habido un error al guardar el vehículo en la base de datos");
        }
    }

    @Transactional
    @PatchMapping("/deactivate/{carId}")
    public ResponseEntity<Object> deactivateCar(HttpServletRequest request, @PathVariable("carId") Long carId){
        UserEntity user = authService.getLoggedInUser(request);
        Map<String, String> results = carService.deactivateCar(user, carId);

        if(results.containsKey("OK")){
            return ResponseEntity.ok(results);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(results);
        }
    }

    @GetMapping()
    public ResponseEntity<Object> getCars(HttpServletRequest request){
        UserEntity user = authService.getLoggedInUser(request);
        List<CarQueryDTO> cars = carService.getCars(user.getId());
        return ResponseEntity.ok(cars);
    }
}
