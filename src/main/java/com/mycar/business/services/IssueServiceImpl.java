package com.mycar.business.services;

import com.mycar.business.controllers.dto.IssueQueryDTO;
import com.mycar.business.repositories.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IssueServiceImpl implements IssueService{
    @Autowired
    IssueRepository issueRepository;
    @Override
    public Page<IssueQueryDTO> getIssues(Long userId, Pageable pageable) {
        Page<IssueQueryDTO> issues = issueRepository.getIssues(userId, pageable);
        return issues;
    }
}
