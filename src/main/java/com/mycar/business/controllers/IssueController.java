package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.IssueCreateDTO;
import com.mycar.business.controllers.dto.IssueQueryDTO;
import com.mycar.business.controllers.utils.ControllerHelper;
import com.mycar.business.entities.IssueEntity;
import com.mycar.business.entities.UserEntity;
import com.mycar.business.services.impl.AuthService;
import com.mycar.business.services.IssueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

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
    public ResponseEntity<Page<IssueQueryDTO>> getIssues(HttpServletRequest request){
        UserEntity user = authService.getLoggedInUser(request);
        Pageable pageable = controllerHelper.getPageable(request);

        Page<IssueQueryDTO> issues = issueService.getIssues(user.getId(), pageable);
        return ResponseEntity.ok(issues);
    }

    @PostMapping()
    public ResponseEntity<Object> create(HttpServletRequest request, @Valid @RequestBody IssueCreateDTO issueCreateDTO){
        UserEntity user = authService.getLoggedInUser(request);
        IssueQueryDTO issue = issueService.createIssue(user, issueCreateDTO);

        if (issue!=null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(issue.getId())
                    .toUri();
            return ResponseEntity.created(location).body(issue);
        } else {
            return ResponseEntity.badRequest().body("Ha habido un error al guardar el issue en la base de datos");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getIssueById(HttpServletRequest request, @PathVariable Long id){
        UserEntity user = authService.getLoggedInUser(request);

        IssueQueryDTO issue = issueService.getIssueById(user.getId(), id);
        if (issue==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el Issue con ID: " + id);
        }
        return ResponseEntity.ok(issue);
    }
}
















