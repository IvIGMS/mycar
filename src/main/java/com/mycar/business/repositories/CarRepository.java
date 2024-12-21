package com.mycar.business.repositories;

import com.mycar.business.controllers.dto.car.CarQueryDTO;
import com.mycar.business.entities.CarEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {
    @Query(" SELECT CASE WHEN COUNT(C) > 0 THEN true ELSE false END" +
            " FROM CarEntity C" +
            " WHERE C.userEntity.id = ?1 " +
            " AND C.id = ?2" +
            " AND C.isActive = true"
    )
    boolean existsByUserIdAndCarIdAndIsActive(Long userId, Long issueId);

    @Transactional
    @Modifying
    @Query("UPDATE CarEntity C " +
            " SET C.isActive = false " +
            " WHERE C.id = ?1")
    int deactivateCar(Long carId);

    @Query("SELECT new com.mycar.business.controllers.dto.car.CarQueryDTO(" +
            " C.id, " +
            " C.companyName, " +
            " C.modelName, " +
            " C.userEntity.id, " +
            " C.km, " +
            " C.isActive, " +
            " C.createdAt, " +
            " C.updatedAt) " +
            " FROM CarEntity C " +
            " WHERE C.userEntity.id = ?1")
    List<CarQueryDTO> getCars(Long userId);

    @Query("SELECT C FROM CarEntity C " +
            " WHERE C.id = ?2 " +
            " AND C.userEntity.id = ?1")
    Optional<CarEntity> findCarByCarIdAndUserId(Long userId, Long carId);

    @Query("SELECT COUNT(C.id)" +
            "FROM CarEntity C " +
            "WHERE C.userEntity.id = ?1")
    int numberOfCarsByUser(Long userId);
}
