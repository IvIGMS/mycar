package com.mycar.business.services.impl;

import com.mycar.business.entities.NotificationEntity;
import com.mycar.business.repositories.NotificationRepository;
import com.mycar.business.services.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }
    @Override
    public NotificationEntity createNotification(NotificationEntity notificationEntity) {
        return notificationRepository.save(notificationEntity);
    }
}
