
package com.example.EmployeeManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EmployeeManagement.employee.Employee;
import com.example.EmployeeManagement.employeeRepo.EmployeeRepository;
import com.example.EmployeeManagement.exception.EmployeeNotFoundException;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository empRepo;

    // 1. Get all employees
    public List<Employee> getAllEmployees() {
        return empRepo.findAll();
    }

    // 2. Get employee by ID
    public Employee getBy(int id) {

        return empRepo.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with ID: " + id
                        )
                );
    }

    // 3. Create employee
    public Employee postedBy(Employee emp) {
        return empRepo.save(emp);
    }

    // 4. Update employee
    public Employee updatedBy(int id, Employee emp) {

        if (!empRepo.existsById(id)) {
            throw new EmployeeNotFoundException(
                    "Employee not found with ID: " + id
            );
        }

        emp.setId(id);

        return empRepo.save(emp);
    }

    // 5. Delete employee
    public void deletedBy(int id) {

        if (!empRepo.existsById(id)) {
            throw new EmployeeNotFoundException(
                    "Employee not found with ID: " + id
            );
        }

        empRepo.deleteById(id);
    }
}
