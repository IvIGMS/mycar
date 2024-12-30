package com.mycar.business.services;

import com.mycar.business.entities.NotificationEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    public NotificationEntity saveNotification(NotificationEntity notificationEntity);
    List<NotificationEntity> createNotifications(List<Long> ids, Long typeId);
    void sendNotification(NotificationEntity notificationEntity);
}
