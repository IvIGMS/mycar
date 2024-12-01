package com.mycar.business.services;

import com.mycar.business.entities.StatusEntity;
import org.springframework.stereotype.Service;

@Service
public interface StatusService {
    StatusEntity getStatusById(Long id);
}
