package com.mycar.business.services.impl;

import com.mycar.business.entities.StatusEntity;
import com.mycar.business.repositories.StatusRepository;
import com.mycar.business.services.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService {
    @Autowired
    StatusRepository statusRepository;

    @Override
    public StatusEntity getStatusById(Long id) {
        return statusRepository.findById(id).orElse(null);
    }
}
