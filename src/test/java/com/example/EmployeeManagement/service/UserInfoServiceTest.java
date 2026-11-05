
package com.example.EmployeeManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.EmployeeManagement.employee.UserInfo;
import com.example.EmployeeManagement.employeeRepo.userInfoRepository;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private userInfoRepository userInfoRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserInfoService userInfoService;

    // Test 1: Successful registration
    @Test
    void testSaveUserSuccess() {

        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("password123");

        when(userInfoRepo.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encryptedPassword");

        when(userInfoRepo.save(any(UserInfo.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserInfo result = userInfoService.saveMethod(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("EMPLOYEE", result.getRole());
        assertEquals("encryptedPassword", result.getPassword());

        verify(userInfoRepo).save(any(UserInfo.class));
    }

    // Test 2: Null username
    @Test
    void testNullUsername() {

        UserInfo user = new UserInfo();
        user.setUsername(null);
        user.setPassword("password123");

        IllegalArgumentException exception =
                assertThrows(
                    IllegalArgumentException.class,
                    () -> userInfoService.saveMethod(user)
                );

        assertEquals("Username cannot be empty",
                exception.getMessage());

        verifyNoInteractions(userInfoRepo, passwordEncoder);
    }

    // Test 3: Blank username
    @Test
    void testBlankUsername() {

        UserInfo user = new UserInfo();
        user.setUsername("   ");
        user.setPassword("password123");

        IllegalArgumentException exception =
                assertThrows(
                    IllegalArgumentException.class,
                    () -> userInfoService.saveMethod(user)
                );

        assertEquals("Username cannot be empty",
                exception.getMessage());

        verifyNoInteractions(userInfoRepo, passwordEncoder);
    }

    // Test 4: Null password
    @Test
    void testNullPassword() {

        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword(null);

        IllegalArgumentException exception =
                assertThrows(
                    IllegalArgumentException.class,
                    () -> userInfoService.saveMethod(user)
                );

        assertEquals("Password cannot be empty",
                exception.getMessage());

        verifyNoInteractions(userInfoRepo, passwordEncoder);
    }

    // Test 5: Blank password
    @Test
    void testBlankPassword() {

        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("   ");

        IllegalArgumentException exception =
                assertThrows(
                    IllegalArgumentException.class,
                    () -> userInfoService.saveMethod(user)
                );

        assertEquals("Password cannot be empty",
                exception.getMessage());

        verifyNoInteractions(userInfoRepo, passwordEncoder);
    }

    // Test 6: Duplicate username
    @Test
    void testDuplicateUsername() {

        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("password123");

        when(userInfoRepo.findByUsername("testuser"))
                .thenReturn(Optional.of(new UserInfo()));

        IllegalArgumentException exception =
                assertThrows(
                    IllegalArgumentException.class,
                    () -> userInfoService.saveMethod(user)
                );

        assertEquals("Username already exists",
                exception.getMessage());

        verify(userInfoRepo, never())
                .save(any(UserInfo.class));

        verifyNoInteractions(passwordEncoder);
    }

    // Test 7: Default role must be EMPLOYEE
    @Test
    void testDefaultRoleAssignment() {

        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("password123");

        // Attempt to register with ADMIN role
        user.setRole("ADMIN");

        when(userInfoRepo.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encryptedPassword");

        when(userInfoRepo.save(any(UserInfo.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserInfo result = userInfoService.saveMethod(user);

        assertEquals("EMPLOYEE", result.getRole());
    }

    // Test 8: Password encryption
    @Test
    void testPasswordEncryption() {

        UserInfo user = new UserInfo();
        user.setUsername("testuser");
        user.setPassword("password123");

        when(userInfoRepo.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encryptedPassword");

        when(userInfoRepo.save(any(UserInfo.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserInfo result = userInfoService.saveMethod(user);

        assertEquals("encryptedPassword",
                result.getPassword());

        verify(passwordEncoder).encode("password123");
    }

    // Test 9: Username trimming
    @Test
    void testUsernameTrimming() {

        UserInfo user = new UserInfo();
        user.setUsername("  testuser  ");
        user.setPassword("password123");

        when(userInfoRepo.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encryptedPassword");

        when(userInfoRepo.save(any(UserInfo.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserInfo result = userInfoService.saveMethod(user);

        assertEquals("testuser", result.getUsername());

        verify(userInfoRepo).findByUsername("testuser");
    }

    // Test 10: ID must be null before saving
    @Test
    void testIdResetBeforeSaving() {

        UserInfo user = new UserInfo();
        user.setId(10L);
        user.setUsername("testuser");
        user.setPassword("password123");

        when(userInfoRepo.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encryptedPassword");

        when(userInfoRepo.save(any(UserInfo.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        userInfoService.saveMethod(user);

        ArgumentCaptor<UserInfo> captor =
                ArgumentCaptor.forClass(UserInfo.class);

        verify(userInfoRepo).save(captor.capture());

        assertNull(captor.getValue().getId());
    }
}
