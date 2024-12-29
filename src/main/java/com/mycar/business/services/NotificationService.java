package com.mycar.business.services;

import com.mycar.business.entities.NotificationEntity;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    public NotificationEntity createNotification(NotificationEntity notificationEntity);
}
