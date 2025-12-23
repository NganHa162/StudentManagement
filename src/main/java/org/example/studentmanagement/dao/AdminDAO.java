package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Admin;

import java.util.Optional;

public interface AdminDAO extends BaseDAO<Admin, Integer> {
    Optional<Admin> findByUserName(String userName);
}


