
package com.example.EmployeeManagement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.EmployeeManagement.employee.Employee;
import com.example.EmployeeManagement.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService empService;

    // 1. Welcome
    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok(
                "Welcome to Employee Management System"
        );
    }

    // 2. Get all employees
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(
                empService.getAllEmployees()
        );
    }

    // 3. Get employee by ID
    @GetMapping("/getBy/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<Employee> getById(
            @PathVariable int id) {

        return ResponseEntity.ok(empService.getBy(id));
    }

    // 4. Create employee
    @PostMapping("/posted")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<Map<String, Object>> posted(
            @Valid @RequestBody Employee emp) {

        Employee savedEmployee = empService.postedBy(emp);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Employee added successfully",
                        "employee", savedEmployee
                ));
    }

    // 5. Update employee
    @PutMapping("/updated/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<Map<String, Object>> updated(
            @PathVariable int id,
            @Valid @RequestBody Employee emp) {

        Employee updatedEmployee =
                empService.updatedBy(id, emp);

        if (updatedEmployee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message",
                            "Employee not found with ID: " + id
                    ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Employee updated successfully",
                "employee", updatedEmployee
        ));
    }

    // 6. Delete employee
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteEmployee(
            @PathVariable int id) {

        empService.deletedBy(id);

        return ResponseEntity.ok(Map.of(
                "message", "Employee deleted successfully"
        ));
    }
}
