package com.mycar.business.controllers.mappers;

import com.mycar.business.controllers.dto.issue.IssueCreateDTO;
import com.mycar.business.entities.IssueEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IssueIssueCreateDTOMapper {
    IssueCreateDTO issueEntityToIssueCreateDTO(IssueEntity issue);
    IssueEntity issueCreateDTOToIssueEntity(IssueCreateDTO issueCreateDTO);

}
