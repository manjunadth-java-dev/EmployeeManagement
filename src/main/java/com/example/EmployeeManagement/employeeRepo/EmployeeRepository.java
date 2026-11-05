package com.example.EmployeeManagement.employeeRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.EmployeeManagement.employee.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{

}
