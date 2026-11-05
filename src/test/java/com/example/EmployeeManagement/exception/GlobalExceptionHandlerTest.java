
package com.example.EmployeeManagement.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    // TEST 1: Employee not found
    @Test
    void testEmployeeNotFound() {

        EmployeeNotFoundException exception =
                new EmployeeNotFoundException(
                        "Employee not found with ID: 99"
                );

        ResponseEntity<Map<String, String>> response =
                handler.handleEmployeeNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND,
                response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(
                "Employee not found with ID: 99",
                response.getBody().get("message")
        );
    }

    // TEST 2: Employee name validation
    @Test
    void testEmployeeNameValidation() {

        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        BindingResult bindingResult =
                mock(BindingResult.class);

        when(exception.getBindingResult())
                .thenReturn(bindingResult);

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(
                        new FieldError(
                                "employee",
                                "employeeName",
                                "Employee name is required"
                        )
                ));

        ResponseEntity<Map<String, Object>> response =
                handler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals("Validation failed",
                response.getBody().get("message"));

        @SuppressWarnings("unchecked")
        Map<String, String> errors =
                (Map<String, String>)
                        response.getBody().get("errors");

        assertEquals(
                "Employee name is required",
                errors.get("employeeName")
        );
    }

    // TEST 3: Multiple validation errors
    @Test
    void testMultipleValidationErrors() {

        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        BindingResult bindingResult =
                mock(BindingResult.class);

        when(exception.getBindingResult())
                .thenReturn(bindingResult);

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(
                        new FieldError(
                                "employee",
                                "employeeName",
                                "Employee name is required"
                        ),
                        new FieldError(
                                "employee",
                                "employeeAddress",
                                "Employee address is required"
                        ),
                        new FieldError(
                                "employee",
                                "phoneNumber",
                                "Phone number must contain exactly 10 digits"
                        )
                ));

        ResponseEntity<Map<String, Object>> response =
                handler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, String> errors =
                (Map<String, String>)
                        response.getBody().get("errors");

        assertEquals(3, errors.size());

        assertEquals(
                "Employee name is required",
                errors.get("employeeName")
        );

        assertEquals(
                "Employee address is required",
                errors.get("employeeAddress")
        );

        assertEquals(
                "Phone number must contain exactly 10 digits",
                errors.get("phoneNumber")
        );
    }
}
