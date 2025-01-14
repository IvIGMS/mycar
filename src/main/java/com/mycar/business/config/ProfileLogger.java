package com.mycar.business.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfileLogger {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @PostConstruct
    public void logActiveProfile() {
        log.info("################## ACTIVE PROFILE: {} ##################", activeProfile.toUpperCase());
    }
}