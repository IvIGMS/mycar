package com.mycar.business.services.impl;

import com.mycar.business.controllers.dto.issue.IssueCreateDTO;
import com.mycar.business.controllers.dto.issue.IssueQueryDTO;
import com.mycar.business.controllers.mappers.IssueIssueQueryDTOMapper;
import com.mycar.business.controllers.mappers.IssueIssueQueryDTOMapperImpl;
import com.mycar.business.entities.*;
import com.mycar.business.repositories.IssueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceImplTest {

    @Mock
    private TypeServiceImpl typeService;

    @Mock
    private StatusServiceImpl statusService;

    @Mock
    private CarServiceImpl carService;

    @Mock
    IssueRepository issueRepository;

    private IssueIssueQueryDTOMapper issueIssueQueryDTOMapper;
    private IssueServiceImpl issueService;

    private IssueEntity issueEntityDistance;
    private IssueEntity issueEntityTime;
    private IssueEntity issueEntity;
    private CarEntity carEntity;
    Page<IssueQueryDTO> issuesPage;
    private Pageable pageable;
    IssueQueryDTO issueQueryDTO;
    IssueCreateDTO issueCreateDTODistance;
    IssueCreateDTO issueCreateDTOTime;
    TypeEntity typeEntityDistance;
    TypeEntity typeEntityTime;
    StatusEntity statusEntity;

    UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        issueIssueQueryDTOMapper = new IssueIssueQueryDTOMapperImpl();
        issueService = new IssueServiceImpl(issueRepository, typeService, statusService, carService, issueIssueQueryDTOMapper);
        pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());

        carEntity = CarEntity.builder().id(1L).companyName("Seat").modelName("Leon").build();

        issueQueryDTO = IssueQueryDTO.builder().name("Cambio de ruedas").id(1L).carId(1L).build();
        issueEntity = IssueEntity.builder().name("Cambio de ruedas").id(1L).carEntity(carEntity).build();
        issueEntityDistance = IssueEntity.builder().name("Revisar pastillas de freno").id(1L).carEntity(carEntity).build();
        issueEntityTime = IssueEntity.builder().name("Revisar nivel de carburante").id(2L).carEntity(carEntity).build();

        List<IssueQueryDTO> issueQueryDTOS = new ArrayList<IssueQueryDTO>();
        issueQueryDTOS.add(issueQueryDTO);

        issuesPage = new PageImpl<>(issueQueryDTOS, pageable, 1);

        issueCreateDTODistance = IssueCreateDTO.builder().name("Revisar pastillas de freno").currentDistance(20000)
                .notificationDistance(50000).typeId(1L).carId(1L).build();

        issueCreateDTOTime = IssueCreateDTO.builder().name("Revisar nivel de carburante").notificationDateDays(30)
                .typeId(2L).carId(1L).build();

        typeEntityDistance = TypeEntity.builder().id(1L).build();
        typeEntityTime = TypeEntity.builder().id(2L).build();

        statusEntity= StatusEntity.builder().id(1L).build();

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test_user");
        userEntity.setEmail("inventado@gmail.com");
        userEntity.setFirstName("Usuario");
        userEntity.setLastName("Invitado");
    }

    @Test
    void getIssues_ok() {
        Mockito.when(issueRepository.getIssues(1L, pageable)).thenReturn(issuesPage);

        Page<IssueQueryDTO> results = issueService.getIssues(1L, pageable);

        assertNotNull(results);
        assertEquals("Cambio de ruedas", results.getContent().get(0).getName());
        assertEquals(1L, results.getContent().get(0).getCarId());
    }

    @Test
    void createIssue_okDistance() {
        Mockito.when(carService.getCarIfThisOwnerUser(Mockito.anyLong(), Mockito.anyLong())).thenReturn(carEntity);
        Mockito.when(typeService.getTypeById(Mockito.anyLong())).thenReturn(typeEntityDistance);
        Mockito.when(statusService.getStatusById(Mockito.anyLong())).thenReturn(statusEntity);
        Mockito.when(issueRepository.save(Mockito.any())).thenReturn(issueEntityDistance);

        IssueQueryDTO results = issueService.createIssue(userEntity, issueCreateDTODistance);

        assertNotNull(results);
        assertEquals("Revisar pastillas de freno", results.getName());
        assertEquals(1L, results.getCarId());
        assertEquals(1L, results.getId());
    }

    @Test
    void createIssue_koDistance() {
        Mockito.when(carService.getCarIfThisOwnerUser(Mockito.anyLong(), Mockito.anyLong())).thenReturn(carEntity);
        Mockito.when(typeService.getTypeById(Mockito.anyLong())).thenReturn(typeEntityDistance);
        Mockito.when(statusService.getStatusById(Mockito.anyLong())).thenReturn(statusEntity);
        Mockito.when(issueRepository.save(Mockito.any())).thenThrow(new DataIntegrityViolationException("Llave duplicada"));

        IssueQueryDTO results = issueService.createIssue(userEntity, issueCreateDTODistance);

        assertNull(results);
    }

    @Test
    void createIssue_okTime() {
        Mockito.when(carService.getCarIfThisOwnerUser(Mockito.anyLong(), Mockito.anyLong())).thenReturn(carEntity);
        Mockito.when(typeService.getTypeById(Mockito.anyLong())).thenReturn(typeEntityTime);
        Mockito.when(statusService.getStatusById(Mockito.anyLong())).thenReturn(statusEntity);
        Mockito.when(issueRepository.save(Mockito.any())).thenReturn(issueEntityTime);

        IssueQueryDTO results = issueService.createIssue(userEntity, issueCreateDTOTime);

        assertNotNull(results);
        assertEquals("Revisar nivel de carburante", results.getName());
        assertEquals(1L, results.getCarId());
        assertEquals(2L, results.getId());
    }

    @Test
    void createIssue_koTime() {
        Mockito.when(carService.getCarIfThisOwnerUser(Mockito.anyLong(), Mockito.anyLong())).thenReturn(carEntity);
        Mockito.when(typeService.getTypeById(Mockito.anyLong())).thenReturn(typeEntityTime);
        Mockito.when(statusService.getStatusById(Mockito.anyLong())).thenReturn(statusEntity);
        Mockito.when(issueRepository.save(Mockito.any())).thenThrow(new DataIntegrityViolationException("Llave duplicada"));

        IssueQueryDTO results = issueService.createIssue(userEntity, issueCreateDTOTime);

        assertNull(results);
    }

    @Test
    void createIssue_koCarNull() {
        Mockito.when(carService.getCarIfThisOwnerUser(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        IssueQueryDTO results = issueService.createIssue(userEntity, issueCreateDTOTime);
        assertNull(results);
    }

    @Test
    void createIssue_koIssueTypeIdNull() {
        issueCreateDTOTime.setTypeId(null);
        Mockito.when(carService.getCarIfThisOwnerUser(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        IssueQueryDTO results = issueService.createIssue(userEntity, issueCreateDTOTime);
        assertNull(results);
    }

    @Test
    void getIssueById_ok() {
        Mockito.when(issueRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(issueEntity));
        Mockito.when(issueRepository.existIssueByUsername(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);

        IssueQueryDTO results = issueService.getIssueById(1L, 1L);

        assertNotNull(results);
        assertEquals("Cambio de ruedas", results.getName());
        assertEquals(1L, results.getCarId());
    }

    @Test
    void getIssueById_ko() {
        Mockito.when(issueRepository.existIssueByUsername(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
        IssueQueryDTO results = issueService.getIssueById(1L, 1L);
        assertNull(results);
    }
}