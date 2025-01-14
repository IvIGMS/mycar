package com.mycar.business.repositories;

import com.mycar.business.entities.NotificationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationTypeEntity, Long> {
}
