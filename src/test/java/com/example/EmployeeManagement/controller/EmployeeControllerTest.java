
package com.example.EmployeeManagement.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.EmployeeManagement.employee.Employee;
import com.example.EmployeeManagement.exception.EmployeeNotFoundException;
import com.example.EmployeeManagement.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService empService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;

    @BeforeEach
    void setUp() {

        employee = new Employee();

        employee.setId(8);
        employee.setEmployeeName("ravi");
        employee.setEmployeeAddress("hyderabad");
        employee.setPhoneNumber("9876543210");
    }

    // 1. Test welcome endpoint
    @Test
    void testWelcome() {

        ResponseEntity<String> response =
                employeeController.welcome();

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertEquals(
                "Welcome to Employee Management System",
                response.getBody()
        );
    }

    // 2. Test GET all employees
    @Test
    void testGetAll() {

        when(empService.getAllEmployees())
                .thenReturn(List.of(employee));

        ResponseEntity<List<Employee>> response =
                employeeController.getAll();

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(1, response.getBody().size());

        assertEquals(
                "ravi",
                response.getBody().get(0).getEmployeeName()
        );

        verify(empService).getAllEmployees();
    }

    // 3. Test GET employee by ID
    @Test
    void testGetById() {

        when(empService.getBy(8))
                .thenReturn(employee);

        ResponseEntity<Employee> response =
                employeeController.getById(8);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                8,
                response.getBody().getId()
        );

        verify(empService).getBy(8);
    }

    // 4. Test POST create employee
    @Test
    void testPosted() {

        when(empService.postedBy(employee))
                .thenReturn(employee);

        ResponseEntity<Map<String, Object>> response =
                employeeController.posted(employee);

        assertEquals(
                HttpStatus.CREATED,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "Employee added successfully",
                response.getBody().get("message")
        );

        Employee savedEmployee =
                (Employee) response.getBody().get("employee");

        assertEquals(
                "ravi",
                savedEmployee.getEmployeeName()
        );

        verify(empService).postedBy(employee);
    }

    // 5. Test PUT update employee
    @Test
    void testUpdated() {

        when(empService.updatedBy(8, employee))
                .thenReturn(employee);

        ResponseEntity<Map<String, Object>> response =
                employeeController.updated(8, employee);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "Employee updated successfully",
                response.getBody().get("message")
        );

        Employee updatedEmployee =
                (Employee) response.getBody().get("employee");

        assertEquals(
                8,
                updatedEmployee.getId()
        );

        verify(empService).updatedBy(8, employee);
    }

    // 6. Test PUT when employee is not found
    @Test
    void testUpdatedNotFound() {

        when(empService.updatedBy(99, employee))
                .thenThrow(
                        new EmployeeNotFoundException(
                                "Employee not found with ID: 99"
                        )
                );

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeController.updated(99, employee)
                );

        assertEquals(
                "Employee not found with ID: 99",
                exception.getMessage()
        );

        verify(empService).updatedBy(99, employee);
    }

    // 7. Test DELETE employee
    @Test
    void testDeleteEmployee() {

        ResponseEntity<Map<String, String>> response =
                employeeController.deleteEmployee(8);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "Employee deleted successfully",
                response.getBody().get("message")
        );

        verify(empService).deletedBy(8);
    }
}
