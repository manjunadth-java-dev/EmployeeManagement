
package com.example.EmployeeManagement.config;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.EmployeeManagement.controller.EmployeeController;
import com.example.EmployeeManagement.employee.Employee;
import com.example.EmployeeManagement.employeeRepo.userInfoRepository;
import com.example.EmployeeManagement.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
@Import(SecurityConfig.class)
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock EmployeeService
    @MockitoBean
    private EmployeeService empService;

    // Mock UserInfo repository to avoid MySQL
    @MockitoBean
    private userInfoRepository userInfoRepo;

    // Mock JWT decoder to avoid real token decoding
    @MockitoBean
    private JwtDecoder jwtDecoder;

    // Test 1: Public endpoint
    @Test
    void welcomeWithoutToken() throws Exception {

        mockMvc.perform(get("/welcome"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                    "Welcome to Employee Management System"
                ));
    }

    // Test 2: Without JWT token -> 401
    @Test
    void getAllWithoutToken() throws Exception {

        mockMvc.perform(get("/getAll"))
                .andExpect(status().isUnauthorized());
    }

    // Test 3: ADMIN can view employees
    @Test
    void adminCanGetAll() throws Exception {

        when(empService.getAllEmployees())
                .thenReturn(List.of());

        mockMvc.perform(
                get("/getAll")
                .with(jwt().authorities(
                    () -> "ROLE_ADMIN"
                ))
        )
        .andExpect(status().isOk());

        verify(empService).getAllEmployees();
    }

    // Test 4: HR can view employees
    @Test
    void hrCanGetAll() throws Exception {

        when(empService.getAllEmployees())
                .thenReturn(List.of());

        mockMvc.perform(
                get("/getAll")
                .with(jwt().authorities(
                    () -> "ROLE_HR"
                ))
        )
        .andExpect(status().isOk());

        verify(empService).getAllEmployees();
    }

    // Test 5: EMPLOYEE cannot view employees
    @Test
    void employeeCannotGetAll() throws Exception {

        mockMvc.perform(
                get("/getAll")
                .with(jwt().authorities(
                    () -> "ROLE_EMPLOYEE"
                ))
        )
        .andExpect(status().isForbidden());

        verifyNoInteractions(empService);
    }

    // Test 6: ADMIN can delete
    @Test
    void adminCanDelete() throws Exception {

        mockMvc.perform(
                delete("/delete/8")
                .with(jwt().authorities(
                    () -> "ROLE_ADMIN"
                ))
        )
        .andExpect(status().isOk());

        verify(empService).deletedBy(8);
    }

    // Test 7: HR cannot delete
    @Test
    void hrCannotDelete() throws Exception {

        mockMvc.perform(
                delete("/delete/8")
                .with(jwt().authorities(
                    () -> "ROLE_HR"
                ))
        )
        .andExpect(status().isForbidden());

        verifyNoInteractions(empService);
    }

    // Test 8: EMPLOYEE cannot delete
    @Test
    void employeeCannotDelete() throws Exception {

        mockMvc.perform(
                delete("/delete/8")
                .with(jwt().authorities(
                    () -> "ROLE_EMPLOYEE"
                ))
        )
        .andExpect(status().isForbidden());

        verifyNoInteractions(empService);
    }

    // Test 9: No JWT token cannot delete
    @Test
    void deleteWithoutToken() throws Exception {

        mockMvc.perform(delete("/delete/8"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(empService);
    }
}
