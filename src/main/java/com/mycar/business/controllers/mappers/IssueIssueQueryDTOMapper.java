package com.mycar.business.controllers.mappers;

import com.mycar.business.controllers.dto.IssueQueryDTO;
import com.mycar.business.entities.IssueEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IssueIssueQueryDTOMapper {
    IssueEntity issueQueryDTOToIssueEntity(IssueQueryDTO issueQueryDTO);
    IssueQueryDTO issueEntityToIssueQueryDTO(IssueEntity issueQueryDTO);
}
