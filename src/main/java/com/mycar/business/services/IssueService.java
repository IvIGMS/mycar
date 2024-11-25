package com.mycar.business.services;

import com.mycar.business.controllers.dto.IssueQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IssueService {

    Page<IssueQueryDTO> getIssues(Long userId, Pageable pageable);
}
