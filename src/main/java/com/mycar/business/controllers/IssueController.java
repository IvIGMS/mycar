package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.IssueQueryDTO;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.services.AuthService;
import com.mycar.business.services.IssueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
@Slf4j
public class IssueController {
    @Autowired
    private AuthService authService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private ControllerHelper controllerHelper;

    @GetMapping()
    private ResponseEntity<Page<IssueQueryDTO>> getIssues(HttpServletRequest request){
        UserEntity user = authService.getLoggedInUser(request);
        Pageable pageable = controllerHelper.getPageable(request);

        Page<IssueQueryDTO> issues = issueService.getIssues(user.getId(), pageable);
        return ResponseEntity.ok(issues);
    }
}
