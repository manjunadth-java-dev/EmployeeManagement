
package com.example.EmployeeManagement.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.EmployeeManagement.employee.UserInfo;

public class UserInfoUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final String role;

    public UserInfoUserDetails(UserInfo userInfo) {
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        this.role = userInfo.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String normalizedRole = role.trim()
                .toUpperCase(Locale.ROOT);

        if (normalizedRole.startsWith("ROLE_")) {
            normalizedRole = normalizedRole.substring(5);
        }

        if (!normalizedRole.equals("ADMIN")
                && !normalizedRole.equals("HR")
                && !normalizedRole.equals("EMPLOYEE")) {
            throw new IllegalArgumentException(
                    "Invalid user role: " + role
            );
        }

        return Collections.singletonList(
                new SimpleGrantedAuthority(
                        "ROLE_" + normalizedRole
                )
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
