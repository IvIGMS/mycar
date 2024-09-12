package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.IssueCreateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@Slf4j
public class IssueController {
    @GetMapping("/")
    private String sayHello(){
        return "Hola mundo";
    }

    // Fixme: cambiar por un ResponseDTO con los campos que queramos
    @PostMapping("")
    private ResponseEntity<List<IssueCreateDTO>> getIssues(){
        return ResponseEntity.ok(null);
    }
}
