package com.mycar.business.services;

import com.mycar.business.controllers.dto.issue.IssueCreateDTO;
import com.mycar.business.controllers.dto.issue.IssueQueryDTO;
import com.mycar.business.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IssueService {

    Page<IssueQueryDTO> getIssues(Long userId, Pageable pageable);

    IssueQueryDTO createIssue(UserEntity user, IssueCreateDTO issueCreateDTO);

    IssueQueryDTO getIssueById(Long userId, Long issueId);
}
