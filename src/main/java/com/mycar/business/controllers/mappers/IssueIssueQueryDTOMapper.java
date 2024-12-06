package com.mycar.business.controllers.mappers;

import com.mycar.business.controllers.dto.issue.IssueQueryDTO;
import com.mycar.business.entities.IssueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IssueIssueQueryDTOMapper {
    IssueEntity issueQueryDTOToIssueEntity(IssueQueryDTO issueQueryDTO);
    @Mapping(target = "statusId", source = "statusEntity.id")
    @Mapping(target = "statusName", source = "statusEntity.statusName")
    @Mapping(target = "typeId", source = "typeEntity.id")
    @Mapping(target = "typeName", source = "typeEntity.typeName")
    @Mapping(target = "carId", source = "carEntity.id")
    IssueQueryDTO issueEntityToIssueQueryDTO(IssueEntity issueQueryDTO);
}
