package com.example.EmployeeManagement.employeeRepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EmployeeManagement.employee.UserInfo;

@Repository
public interface userInfoRepository extends JpaRepository<UserInfo,Long> {
	public Optional<UserInfo> findByUsername(String username);

}
