package com.mycar.business.controllers.mappers;

import com.mycar.business.controllers.dto.car.CarQueryDTO;
import com.mycar.business.entities.CarEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarCarQueryDTOMapper {
    @Mapping(target = "userId", source = "userEntity.id")
    CarQueryDTO carEntityToCarQueryDTO(CarEntity carEntity);
    CarEntity carQueryDTOToCarEntity(CarQueryDTO carQueryDTO);
}
