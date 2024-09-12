package com.mycar.business.repositories;

import com.mycar.business.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET is_active = true WHERE id = :id",
            nativeQuery = true)
    void activateUser(@Param("id") Long id);
}
