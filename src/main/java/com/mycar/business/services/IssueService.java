package com.mycar.business.services;

import com.mycar.business.controllers.dto.IssueQueryDTO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IssueService {

    List<IssueQueryDTO> getIssues(Long userId);
}
