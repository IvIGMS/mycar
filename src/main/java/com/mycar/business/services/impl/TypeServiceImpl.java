package com.mycar.business.services.impl;

import com.mycar.business.entities.TypeEntity;
import com.mycar.business.repositories.TypeRepository;
import com.mycar.business.services.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    TypeRepository typeRepository;
    @Override
    public TypeEntity getTypeById(Long id) {
        return typeRepository.findById(id).orElse(null);
    }
}
