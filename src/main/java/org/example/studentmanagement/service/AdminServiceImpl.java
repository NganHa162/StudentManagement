package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.AdminDAO;
import org.example.studentmanagement.entity.Admin;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminDAO adminDAO;

    public AdminServiceImpl(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @Override
    public Optional<Admin> findByUserName(String userName) {
        return adminDAO.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy admin: " + username));
        return User.withUsername(admin.getUserName())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
    }
}


