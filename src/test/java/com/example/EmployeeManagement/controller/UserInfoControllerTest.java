
package com.example.EmployeeManagement.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.EmployeeManagement.dto.RegisterRequest;
import com.example.EmployeeManagement.dto.RegisterResponse;
import com.example.EmployeeManagement.employee.UserInfo;
import com.example.EmployeeManagement.service.UserInfoService;

@ExtendWith(MockitoExtension.class)
class UserInfoControllerTest {

    @Mock
    private UserInfoService userService;

    @InjectMocks
    private UserInfoController userInfoController;

    // Test 1: Successful user registration
    @Test
    void testRegisterUserSuccess() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("testPassword123");

        UserInfo savedUser = new UserInfo();
        savedUser.setId(10L);
        savedUser.setUsername("testuser");
        savedUser.setRole("EMPLOYEE");

        when(userService.saveMethod(any(UserInfo.class)))
                .thenReturn(savedUser);

        ResponseEntity<RegisterResponse> response =
                userInfoController.registerUser(request);

        assertEquals(HttpStatus.CREATED,
                response.getStatusCode());

        assertNotNull(response.getBody());

        verify(userService).saveMethod(any(UserInfo.class));
    }

    // Test 2: Verify request data passed to service
    @Test
    void testRegisterUserPassesCorrectData() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("testPassword123");

        UserInfo savedUser = new UserInfo();
        savedUser.setId(10L);
        savedUser.setUsername("testuser");
        savedUser.setRole("EMPLOYEE");

        when(userService.saveMethod(any(UserInfo.class)))
                .thenReturn(savedUser);

        userInfoController.registerUser(request);

        ArgumentCaptor<UserInfo> captor =
                ArgumentCaptor.forClass(UserInfo.class);

        verify(userService).saveMethod(captor.capture());

        UserInfo capturedUser = captor.getValue();

        assertEquals("testuser",
                capturedUser.getUsername());

        assertEquals("testPassword123",
                capturedUser.getPassword());
    }

    // Test 3: Verify registration response role
    @Test
    void testRegisterUserResponseRole() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("testPassword123");

        UserInfo savedUser = new UserInfo();
        savedUser.setId(10L);
        savedUser.setUsername("testuser");
        savedUser.setRole("EMPLOYEE");

        when(userService.saveMethod(any(UserInfo.class)))
                .thenReturn(savedUser);

        ResponseEntity<RegisterResponse> response =
                userInfoController.registerUser(request);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED,
                response.getStatusCode());

        verify(userService).saveMethod(any(UserInfo.class));
    }
}
