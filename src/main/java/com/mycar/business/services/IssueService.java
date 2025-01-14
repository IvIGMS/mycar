package com.mycar.business.services;

import com.mycar.business.controllers.dto.issue.IssueCreateDTO;
import com.mycar.business.controllers.dto.issue.IssueQueryDTO;
import com.mycar.business.entities.IssueEntity;
import com.mycar.business.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IssueService {

    Page<IssueQueryDTO> getIssues(Long userId, Pageable pageable);

    IssueQueryDTO createIssue(UserEntity user, IssueCreateDTO issueCreateDTO);

    IssueQueryDTO getIssueById(Long userId, Long issueId);

    List<IssueEntity> getIssuesByIds(List<Long> issues);

    List<Long> updateIssuesExpiredByDate();
}
