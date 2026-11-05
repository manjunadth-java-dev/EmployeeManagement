
package com.example.EmployeeManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.EmployeeManagement.employee.Employee;
import com.example.EmployeeManagement.employeeRepo.EmployeeRepository;
import com.example.EmployeeManagement.exception.EmployeeNotFoundException;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository empRepo;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {

        employee = new Employee();

        employee.setId(8);
        employee.setEmployeeName("ravi");
        employee.setEmployeeAddress("hyderabad");
        employee.setPhoneNumber("9876543210");
    }

    // TEST 1: Get all employees
    @Test
    void testGetAllEmployees() {

        when(empRepo.findAll())
                .thenReturn(List.of(employee));

        List<Employee> result =
                employeeService.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("ravi",
                result.get(0).getEmployeeName());

        verify(empRepo).findAll();
    }

    // TEST 2: Get existing employee
    @Test
    void testGetById() {

        when(empRepo.findById(8))
                .thenReturn(Optional.of(employee));

        Employee result = employeeService.getBy(8);

        assertEquals(8, result.getId());
        assertEquals("ravi", result.getEmployeeName());

        verify(empRepo).findById(8);
    }

    // TEST 3: Get employee who does not exist
    @Test
    void testGetByIdNotFound() {

        when(empRepo.findById(99))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeService.getBy(99)
                );

        assertEquals(
                "Employee not found with ID: 99",
                exception.getMessage()
        );

        verify(empRepo).findById(99);
    }

    // TEST 4: Add employee
    @Test
    void testPostedBy() {

        when(empRepo.save(employee))
                .thenReturn(employee);

        Employee result =
                employeeService.postedBy(employee);

        assertEquals("ravi", result.getEmployeeName());

        verify(empRepo).save(employee);
    }

    // TEST 5: Update existing employee
    @Test
    void testUpdatedBy() {

        when(empRepo.existsById(8))
                .thenReturn(true);

        when(empRepo.save(employee))
                .thenReturn(employee);

        Employee result =
                employeeService.updatedBy(8, employee);

        assertNotNull(result);
        assertEquals(8, result.getId());

        verify(empRepo).existsById(8);
        verify(empRepo).save(employee);
    }

    // TEST 6: Update employee who does not exist
    @Test
    void testUpdatedByNotFound() {

        when(empRepo.existsById(99))
                .thenReturn(false);

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeService.updatedBy(99, employee)
                );

        assertEquals(
                "Employee not found with ID: 99",
                exception.getMessage()
        );

        verify(empRepo).existsById(99);
        verify(empRepo, never()).save(any(Employee.class));
    }

    // TEST 7: Delete existing employee
    @Test
    void testDeletedBy() {

        when(empRepo.existsById(8))
                .thenReturn(true);

        employeeService.deletedBy(8);

        verify(empRepo).existsById(8);
        verify(empRepo).deleteById(8);
    }

    // TEST 8: Delete employee who does not exist
    @Test
    void testDeletedByNotFound() {

        when(empRepo.existsById(99))
                .thenReturn(false);

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeService.deletedBy(99)
                );

        assertEquals(
                "Employee not found with ID: 99",
                exception.getMessage()
        );

        verify(empRepo).existsById(99);
        verify(empRepo, never()).deleteById(anyInt());
    }
}
