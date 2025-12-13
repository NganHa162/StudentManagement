package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AdminService extends UserDetailsService {
    Optional<Admin> findByUserName(String userName);
}


