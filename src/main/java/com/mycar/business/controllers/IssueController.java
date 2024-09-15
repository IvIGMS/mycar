package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.IssueCreateDTO;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mycar.business.entities.UserEntity;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@Slf4j
public class IssueController {
    @Autowired
    private AuthService authService;

    @GetMapping()
    private ResponseEntity<List<IssueCreateDTO>> getIssues(HttpServletRequest request){
        UserEntity user = authService.getLoggedInUser(request);
        return ResponseEntity.ok(null);
    }
}
