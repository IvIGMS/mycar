package com.mycar.business.services;

import com.mycar.business.entities.TypeEntity;
import org.springframework.stereotype.Service;

@Service
public interface TypeService {
    TypeEntity getTypeById(Long id);
}
