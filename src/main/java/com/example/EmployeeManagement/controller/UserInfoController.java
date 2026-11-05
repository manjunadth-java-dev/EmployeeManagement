
package com.example.EmployeeManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.EmployeeManagement.dto.RegisterRequest;
import com.example.EmployeeManagement.dto.RegisterResponse;
import com.example.EmployeeManagement.employee.UserInfo;
import com.example.EmployeeManagement.service.UserInfoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userService;

    @PostMapping("/userSaved")
    public ResponseEntity<RegisterResponse> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        UserInfo userInfo = new UserInfo();

        userInfo.setUsername(request.getUsername());
        userInfo.setPassword(request.getPassword());

        UserInfo savedUser = userService.saveMethod(userInfo);

        RegisterResponse response = new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole(),
                "User registered successfully"
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }
}
