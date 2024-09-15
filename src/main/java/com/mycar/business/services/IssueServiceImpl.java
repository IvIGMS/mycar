package com.mycar.business.services;

import com.mycar.business.controllers.dto.IssueQueryDTO;
import com.mycar.business.repositories.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueServiceImpl implements IssueService{
    @Autowired
    IssueRepository issueRepository;
    @Override
    public List<IssueQueryDTO> getIssues(Long userId) {
        List<IssueQueryDTO> issues = issueRepository.getIssues(userId);
        // Fixme: separar en 2 objetos: notificacion por KMS o por TIME
        return issues;
    }
}
