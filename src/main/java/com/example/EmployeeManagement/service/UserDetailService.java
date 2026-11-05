package com.example.EmployeeManagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.EmployeeManagement.dto.UserInfoUserDetails;
import com.example.EmployeeManagement.employee.UserInfo;
import com.example.EmployeeManagement.employeeRepo.userInfoRepository;

public class UserDetailService implements UserDetailsService {
@Autowired
private userInfoRepository userInfoRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<UserInfo> userInfo=userInfoRepo.findByUsername(username);
		return userInfo.map(UserInfoUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}

}
