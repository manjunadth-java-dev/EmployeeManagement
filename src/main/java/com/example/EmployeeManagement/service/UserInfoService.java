
package com.example.EmployeeManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.EmployeeManagement.employee.UserInfo;
import com.example.EmployeeManagement.employeeRepo.userInfoRepository;

@Service
public class UserInfoService {

    @Autowired
    private userInfoRepository userInfoRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfo saveMethod(UserInfo userInfo) {

        // Step 1: Validate username
        if (userInfo.getUsername() == null ||
                userInfo.getUsername().isBlank()) {
            throw new IllegalArgumentException(
                    "Username cannot be empty");
        }

        // Step 2: Validate password
        if (userInfo.getPassword() == null ||
                userInfo.getPassword().isBlank()) {
            throw new IllegalArgumentException(
                    "Password cannot be empty");
        }

        // Step 3: Check whether username already exists
        String username = userInfo.getUsername().trim();

        if (userInfoRepo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(
                    "Username already exists");
        }

        // Step 4: Assign safe default role
        // Never trust a role provided during public registration
        userInfo.setRole("EMPLOYEE");

        // Step 5: Encrypt password using BCrypt
        userInfo.setPassword(
                passwordEncoder.encode(userInfo.getPassword())
        );

        // Step 6: Save user in MySQL
        userInfo.setId(null);
        userInfo.setUsername(username);

        return userInfoRepo.save(userInfo);
    }
}
