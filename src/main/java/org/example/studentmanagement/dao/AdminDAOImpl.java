package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Admin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AdminDAOImpl extends BaseDAOImpl<Admin, Integer> implements AdminDAO {

    private final List<Admin> admins = new ArrayList<>();

    public AdminDAOImpl(PasswordEncoder passwordEncoder) {
        admins.add(new Admin(1000, "admin", passwordEncoder.encode("admin123"),
                "Admin", "System", "admin@example.com"));
    }

    @Override
    public void save(Admin entity) {
        admins.removeIf(admin -> admin.getId() == entity.getId());
        admins.add(entity);
    }

    @Override
    public Admin findById(Integer id) {
        return admins.stream()
                .filter(admin -> admin.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Admin> findAll() {
        return List.copyOf(admins);
    }

    @Override
    public void deleteById(Integer id) {
        admins.removeIf(admin -> admin.getId() == id);
    }

    @Override
    public void delete(Admin entity) {
        admins.remove(entity);
    }

    @Override
    public Optional<Admin> findByUserName(String userName) {
        return admins.stream()
                .filter(admin -> admin.getUserName().equalsIgnoreCase(userName))
                .findFirst();
    }
}


