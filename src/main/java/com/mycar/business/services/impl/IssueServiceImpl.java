package com.mycar.business.services.impl;

import com.mycar.business.controllers.dto.issue.IssueCreateDTO;
import com.mycar.business.controllers.dto.issue.IssueQueryDTO;
import com.mycar.business.controllers.mappers.IssueIssueQueryDTOMapper;
import com.mycar.business.entities.CarEntity;
import com.mycar.business.entities.IssueEntity;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.repositories.IssueRepository;
import com.mycar.business.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final TypeService typeService;
    private final StatusService statusService;
    private final CarService carService;
    private final IssueIssueQueryDTOMapper issueIssueQueryDTOMapper;

    public IssueServiceImpl(IssueRepository issueRepository, TypeService typeService, StatusService statusService, CarService carService, IssueIssueQueryDTOMapper issueIssueQueryDTOMapper) {
        this.issueRepository = issueRepository;
        this.typeService = typeService;
        this.statusService = statusService;
        this.carService = carService;
        this.issueIssueQueryDTOMapper = issueIssueQueryDTOMapper;
    }

    @Override
    public Page<IssueQueryDTO> getIssues(Long userId, Pageable pageable) {
        Page<IssueQueryDTO> issues = issueRepository.getIssues(userId, pageable);
        return issues;
    }

    @Override
    public IssueQueryDTO createIssue(UserEntity user, IssueCreateDTO issueCreateDTO) {
        IssueEntity issueSaved = null;
        CarEntity car = carService.getCarIfThisOwnerUser(user.getId(), issueCreateDTO.getCarId());

        if(issueCreateDTO.getTypeId()!=null && car!=null){
            if(issueCreateDTO.getTypeId()==1){
                // Type distance
                validateIssueDistance(issueCreateDTO);

                IssueEntity issue = IssueEntity.builder()
                        .name(issueCreateDTO.getName())
                        .description(issueCreateDTO.getDescription())
                        .typeEntity(typeService.getTypeById(issueCreateDTO.getTypeId()))
                        .statusEntity(statusService.getStatusById(1L))
                        .currentDistance(issueCreateDTO.getCurrentDistance())
                        .notificationDistance(issueCreateDTO.getNotificationDistance())
                        .carEntity(car)
                        .build();

                try{
                    issueSaved = issueRepository.save(issue);
                    log.info("Issue con tipo distance guardado correctamente");
                } catch (DataIntegrityViolationException e){
                    log.error("El issue no ha podido guardarse, tiene la llave duplicada");
                }
            } else {
                // Type time
                validateIssueTime(issueCreateDTO);

                IssueEntity issue = IssueEntity.builder()
                        .name(issueCreateDTO.getName())
                        .description(issueCreateDTO.getDescription())
                        .typeEntity(typeService.getTypeById(issueCreateDTO.getTypeId()))
                        .statusEntity(statusService.getStatusById(1L))
                        .notificationDate(calculateTime(issueCreateDTO.getNotificationDateDays()))
                        .carEntity(car)
                        .build();
                try{
                    issueSaved = issueRepository.save(issue);
                    log.info("Issue con tipo time guardado correctamente");
                } catch (DataIntegrityViolationException e){
                    log.error("El issue no ha podido guardarse, tiene la llave duplicada");
                }
            }
        }
        return issueIssueQueryDTOMapper.issueEntityToIssueQueryDTO(issueSaved);
    }

    @Override
    public IssueQueryDTO getIssueById(Long userId, Long issueId) {
        if(existIssueByUsername(userId, issueId)){
            Optional<IssueEntity> issue = issueRepository.findById(issueId);
            return issueIssueQueryDTOMapper.issueEntityToIssueQueryDTO(issue.orElse(null));
        }
        return null;
    }

    private boolean existIssueByUsername(Long userId, Long issueId) {
        return issueRepository.existIssueByUsername(userId, issueId);
    }

    private LocalDateTime calculateTime(int days) {
        LocalDateTime now = LocalDateTime.now();
        return now.plusDays(days);
    }

    private void validateIssueDistance(IssueCreateDTO issueCreateDTO) {
        if(issueCreateDTO.getTypeId()!=1 || issueCreateDTO.getName()==null || issueCreateDTO.getNotificationDistance()==null || issueCreateDTO.getCurrentDistance()==null){
            throw new IllegalArgumentException("No has introducido los campos necesarios");
        }
    }

    private void validateIssueTime(IssueCreateDTO issueCreateDTO) {
        if(issueCreateDTO.getTypeId()!=2 || issueCreateDTO.getName()==null || issueCreateDTO.getNotificationDateDays()==null){
            throw new IllegalArgumentException("No has introducido los campos necesarios");
        }
    }
}
