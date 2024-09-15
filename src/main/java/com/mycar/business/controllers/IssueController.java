package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.IssueQueryDTO;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.services.AuthService;
import com.mycar.business.services.IssueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@Slf4j
public class IssueController {
    @Autowired
    private AuthService authService;

    @Autowired
    private IssueService issueService;

    @GetMapping()
    private ResponseEntity<List<IssueQueryDTO>> getIssues(HttpServletRequest request){
        UserEntity user = authService.getLoggedInUser(request);
        List<IssueQueryDTO> issues = issueService.getIssues(user.getId());
        return ResponseEntity.ok(issues);
    }
}
