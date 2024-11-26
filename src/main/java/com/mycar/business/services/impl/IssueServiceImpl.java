package com.mycar.business.services.impl;

import com.mycar.business.controllers.dto.IssueCreateDTO;
import com.mycar.business.controllers.dto.IssueQueryDTO;
import com.mycar.business.entities.IssueEntity;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.repositories.IssueRepository;
import com.mycar.business.services.IssueService;
import com.mycar.business.services.StatusService;
import com.mycar.business.services.TypeService;
import com.mycar.business.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class IssueServiceImpl implements IssueService {
    @Autowired
    IssueRepository issueRepository;

    @Autowired
    UserService userService;

    @Autowired
    TypeService typeService;

    @Autowired
    StatusService statusService;

    @Override
    public Page<IssueQueryDTO> getIssues(Long userId, Pageable pageable) {
        Page<IssueQueryDTO> issues = issueRepository.getIssues(userId, pageable);
        return issues;
    }

    @Override
    public IssueQueryDTO createIssue(UserEntity user, IssueCreateDTO issueCreateDTO) {
        IssueEntity issueSaved;
        if(issueCreateDTO.getTypeId()!=null){
            if(issueCreateDTO.getTypeId()==1){
                // Type distance
                validateIssueDistance(issueCreateDTO);
                // Todo: hacer lo mismo que en el otro pero para el de distance
                issueSaved = issueRepository.save(IssueEntity.builder().build()); // Change obj
                log.info("Issue con tipo distance guardado correctamente");
            } else {
                // Type time
                validateIssueTime(issueCreateDTO);

                IssueEntity issue = IssueEntity.builder()
                        .name(issueCreateDTO.getName())
                        .description(issueCreateDTO.getDescription())
                        .typeEntity(typeService.getTypeById(issueCreateDTO.getTypeId()))
                        .statusEntity(statusService.getStatusById(1L))
                        .notificationDate(calculateDistance(issueCreateDTO.getNotificationDateDays()))
                        .userEntity(user)
                        .build();

                issueSaved = issueRepository.save(issue);
                log.info("Issue con tipo time guardado correctamente");
            }
        }
        // Todo: pasar el IssueSaved al DTO que tenemos que sacar por pantalla.
        return null;
    }

    private LocalDateTime calculateDistance(int days) {
        LocalDateTime now = LocalDateTime.now();
        return now.plusDays(days);
    }

    private void validateIssueTime(IssueCreateDTO issueCreateDTO) {
        if(issueCreateDTO.getTypeId()==null || issueCreateDTO.getName()==null || issueCreateDTO.getNotificationDateDays()==null){
            throw new IllegalArgumentException("No has introducido los campos necesarios");
        }
    }

    private void validateIssueDistance(IssueCreateDTO issueCreateDTO) {
        if(issueCreateDTO.getTypeId()==null || issueCreateDTO.getName()==null || issueCreateDTO.getNotificationDistance()==null || issueCreateDTO.getCurrentDistance()==null){
            throw new IllegalArgumentException("No has introducido los campos necesarios");
        }
    }
}
