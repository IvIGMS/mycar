package com.mycar.business.services.impl;

import com.mycar.business.entities.NotificationTypeEntity;
import com.mycar.business.repositories.NotificationTypeRepository;
import com.mycar.business.services.NotificationTypeService;
import org.springframework.stereotype.Service;

@Service
public class NotificationTypeServiceImpl implements NotificationTypeService {
    private final NotificationTypeRepository notificationTypeRepository;

    public NotificationTypeServiceImpl(NotificationTypeRepository notificationTypeRepository) {
        this.notificationTypeRepository = notificationTypeRepository;
    }

    @Override
    public NotificationTypeEntity findById(Long id) {
        return notificationTypeRepository.findById(id).orElse(null);
    }
}
