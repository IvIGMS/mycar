package com.mycar.business.services;

import com.mycar.business.controllers.dto.IssueCreateDTO;
import com.mycar.business.controllers.dto.IssueQueryDTO;
import com.mycar.business.entities.IssueEntity;
import com.mycar.business.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IssueService {

    Page<IssueQueryDTO> getIssues(Long userId, Pageable pageable);

    IssueQueryDTO createIssue(UserEntity user, IssueCreateDTO issueCreateDTO);

    IssueQueryDTO getIssueById(Long userId, Long issueId);
}
