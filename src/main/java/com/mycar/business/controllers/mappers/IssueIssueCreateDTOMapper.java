package com.mycar.business.controllers.mappers;

import com.mycar.business.controllers.dto.IssueCreateDTO;
import com.mycar.business.controllers.dto.UserDTO;
import com.mycar.business.entities.IssueEntity;
import com.mycar.business.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IssueIssueCreateDTOMapper {
    IssueCreateDTO issueEntityToIssueCreateDTO(IssueEntity issue);
    IssueEntity IssueCreateDTOToIssueEntity(IssueCreateDTO issueCreateDTO);
}
