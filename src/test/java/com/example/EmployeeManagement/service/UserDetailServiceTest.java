
package com.example.EmployeeManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.EmployeeManagement.employee.UserInfo;
import com.example.EmployeeManagement.employeeRepo.userInfoRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {

    @Mock
    private userInfoRepository userInfoRepo;

    @InjectMocks
    private UserDetailService userDetailService;

    // Test 1: Username exists
    @Test
    void testLoadUserByUsernameSuccess() {

        UserInfo user = new UserInfo();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("encryptedPassword");
        user.setRole("ADMIN");

        when(userInfoRepo.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        UserDetails result =
                userDetailService.loadUserByUsername("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals("encryptedPassword", result.getPassword());

        assertTrue(
                result.getAuthorities().stream()
                    .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"))
        );

        verify(userInfoRepo).findByUsername("admin");
    }

    // Test 2: Username does not exist
    @Test
    void testLoadUserByUsernameNotFound() {

        when(userInfoRepo.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception =
                assertThrows(
                    UsernameNotFoundException.class,
                    () -> userDetailService
                            .loadUserByUsername("unknown")
                );

        assertEquals(
                "User not found: unknown",
                exception.getMessage()
        );

        verify(userInfoRepo).findByUsername("unknown");
    }

    // Test 3: HR user loads successfully
    @Test
    void testLoadHrUser() {

        UserInfo user = new UserInfo();
        user.setId(2L);
        user.setUsername("hruser");
        user.setPassword("encryptedPassword");
        user.setRole("HR");

        when(userInfoRepo.findByUsername("hruser"))
                .thenReturn(Optional.of(user));

        UserDetails result =
                userDetailService.loadUserByUsername("hruser");

        assertNotNull(result);
        assertEquals("hruser", result.getUsername());

        assertTrue(
                result.getAuthorities().stream()
                    .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_HR"))
        );

        verify(userInfoRepo).findByUsername("hruser");
    }

    // Test 4: Employee user loads successfully
    @Test
    void testLoadEmployeeUser() {

        UserInfo user = new UserInfo();
        user.setId(3L);
        user.setUsername("employee");
        user.setPassword("encryptedPassword");
        user.setRole("EMPLOYEE");

        when(userInfoRepo.findByUsername("employee"))
                .thenReturn(Optional.of(user));

        UserDetails result =
                userDetailService.loadUserByUsername("employee");

        assertNotNull(result);
        assertEquals("employee", result.getUsername());

        assertTrue(
                result.getAuthorities().stream()
                    .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_EMPLOYEE"))
        );

        verify(userInfoRepo).findByUsername("employee");
    }
}
