package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.user.UserDTO;
import com.mycar.business.controllers.dto.user.UserRegisterDTO;
import com.mycar.business.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRegisterDTO userRegisterDTO){
        UserDTO userResult = userService.createUser(userRegisterDTO);
        return ResponseEntity.ok().body(userResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        UserDTO userResult = userService.getUserById(id);
        return ResponseEntity.ok().body(userResult);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activateUser(@PathVariable Long id){
        String result = userService.activateUser(id);
        return ResponseEntity.ok().body(result);
    }
}
