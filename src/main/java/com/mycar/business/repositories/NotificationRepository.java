package com.mycar.business.repositories;

import com.mycar.business.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Modifying
    @Query("UPDATE NotificationEntity N SET N.isSent = true WHERE N.id = ?1")
    void activeIsSend(Long id);
}
