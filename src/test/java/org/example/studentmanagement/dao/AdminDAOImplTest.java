package org.example.studentmanagement.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.studentmanagement.entity.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class AdminDAOImplTest {

    private AdminDAOImpl adminDAO;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> "encoded_" + invocation.getArgument(0));
        adminDAO = new AdminDAOImpl(passwordEncoder);
    }

    @Test
    void constructor_createsDefaultAdmin() {
        List<Admin> admins = adminDAO.findAll();

        assertEquals(1, admins.size(), "Should have one default admin");
        Admin defaultAdmin = admins.get(0);
        assertEquals(1000, defaultAdmin.getId());
        assertEquals("admin", defaultAdmin.getUserName());
        assertEquals("encoded_admin123", defaultAdmin.getPassword());
        assertEquals("Admin", defaultAdmin.getFirstName());
        assertEquals("System", defaultAdmin.getLastName());
        assertEquals("admin@example.com", defaultAdmin.getEmail());
    }

    @Test
    void save_insertsNewAdmin() {
        Admin newAdmin = new Admin(2000, "john_admin", "password123", "John", "Doe", "john@example.com");

        adminDAO.save(newAdmin);

        List<Admin> admins = adminDAO.findAll();
        assertEquals(2, admins.size(), "Should have 2 admins after save");
        assertTrue(admins.stream().anyMatch(a -> a.getId() == 2000 && a.getUserName().equals("john_admin")));
    }

    @Test
    void save_updatesExistingAdmin() {
        Admin admin = adminDAO.findById(1000);
        assertNotNull(admin);

        admin.setFirstName("Updated");
        admin.setLastName("Name");
        admin.setEmail("updated@example.com");
        adminDAO.save(admin);

        Admin updated = adminDAO.findById(1000);
        assertNotNull(updated);
        assertEquals("Updated", updated.getFirstName());
        assertEquals("Name", updated.getLastName());
        assertEquals("updated@example.com", updated.getEmail());
        assertEquals("admin", updated.getUserName());

        List<Admin> allAdmins = adminDAO.findAll();
        assertEquals(1, allAdmins.size(), "Should still have only 1 admin after update");
    }

    @Test
    void save_replacesAdminWithSameId() {
        Admin replacement = new Admin(1000, "new_admin", "newpass", "New", "Admin", "new@example.com");

        adminDAO.save(replacement);

        List<Admin> admins = adminDAO.findAll();
        assertEquals(1, admins.size(), "Should have only 1 admin after replacement");

        Admin found = adminDAO.findById(1000);
        assertNotNull(found);
        assertEquals("new_admin", found.getUserName());
        assertEquals("New", found.getFirstName());
        assertEquals("Admin", found.getLastName());
        assertEquals("new@example.com", found.getEmail());
    }

    @Test
    void findById_returnsAdminWhenExists() {
        Admin found = adminDAO.findById(1000);

        assertNotNull(found);
        assertEquals(1000, found.getId());
        assertEquals("admin", found.getUserName());
        assertEquals("Admin", found.getFirstName());
        assertEquals("System", found.getLastName());
    }

    @Test
    void findById_returnsNullWhenAdminDoesNotExist() {
        Admin found = adminDAO.findById(9999);

        assertNull(found, "Should return null when admin does not exist");
    }

    @Test
    void findAll_returnsAllAdmins() {
        Admin admin1 = new Admin(2000, "admin1", "pass1", "First", "Admin", "admin1@example.com");
        Admin admin2 = new Admin(3000, "admin2", "pass2", "Second", "Admin", "admin2@example.com");
        Admin admin3 = new Admin(4000, "admin3", "pass3", "Third", "Admin", "admin3@example.com");

        adminDAO.save(admin1);
        adminDAO.save(admin2);
        adminDAO.save(admin3);

        List<Admin> admins = adminDAO.findAll();

        assertEquals(4, admins.size(), "Should return all admins including default");
        assertTrue(admins.stream().anyMatch(a -> a.getId() == 1000));
        assertTrue(admins.stream().anyMatch(a -> a.getId() == 2000));
        assertTrue(admins.stream().anyMatch(a -> a.getId() == 3000));
        assertTrue(admins.stream().anyMatch(a -> a.getId() == 4000));
    }

    @Test
    void findAll_returnsImmutableCopy() {
        List<Admin> admins1 = adminDAO.findAll();
        List<Admin> admins2 = adminDAO.findAll();

        assertEquals(admins1.size(), admins2.size());
        assertTrue(admins1 != admins2, "Should return different list instances");
    }

    @Test
    void deleteById_removesAdmin() {
        Admin newAdmin = new Admin(5000, "temp_admin", "temppass", "Temp", "Admin", "temp@example.com");
        adminDAO.save(newAdmin);

        assertEquals(2, adminDAO.findAll().size(), "Should have 2 admins before delete");

        adminDAO.deleteById(5000);

        assertEquals(1, adminDAO.findAll().size(), "Should have 1 admin after delete");
        assertNull(adminDAO.findById(5000), "Deleted admin should not be found");
    }

    @Test
    void deleteById_doesNothingWhenAdminDoesNotExist() {
        int initialSize = adminDAO.findAll().size();

        adminDAO.deleteById(9999);

        assertEquals(initialSize, adminDAO.findAll().size(), "Size should remain the same");
    }

    @Test
    void delete_removesAdminByEntity() {
        Admin admin = adminDAO.findById(1000);
        assertNotNull(admin);

        adminDAO.delete(admin);

        assertEquals(0, adminDAO.findAll().size(), "Should have 0 admins after delete");
        assertNull(adminDAO.findById(1000), "Deleted admin should not be found");
    }

    @Test
    void delete_doesNothingWhenAdminNotInList() {
        Admin nonExistentAdmin = new Admin(9999, "ghost", "pass", "Ghost", "Admin", "ghost@example.com");
        int initialSize = adminDAO.findAll().size();

        adminDAO.delete(nonExistentAdmin);

        assertEquals(initialSize, adminDAO.findAll().size(), "Size should remain the same");
    }

    @Test
    void findByUserName_returnsAdminWhenExists() {
        Optional<Admin> found = adminDAO.findByUserName("admin");

        assertTrue(found.isPresent(), "Should find admin by username");
        assertEquals(1000, found.get().getId());
        assertEquals("admin", found.get().getUserName());
        assertEquals("Admin", found.get().getFirstName());
    }

    @Test
    void findByUserName_isCaseInsensitive() {
        Optional<Admin> found1 = adminDAO.findByUserName("ADMIN");
        Optional<Admin> found2 = adminDAO.findByUserName("Admin");
        Optional<Admin> found3 = adminDAO.findByUserName("aDmIn");

        assertTrue(found1.isPresent(), "Should find admin with uppercase");
        assertTrue(found2.isPresent(), "Should find admin with mixed case");
        assertTrue(found3.isPresent(), "Should find admin with random case");

        assertEquals(1000, found1.get().getId());
        assertEquals(1000, found2.get().getId());
        assertEquals(1000, found3.get().getId());
    }

    @Test
    void findByUserName_returnsEmptyWhenNotFound() {
        Optional<Admin> found = adminDAO.findByUserName("nonexistent");

        assertTrue(found.isEmpty(), "Should return empty optional when admin not found");
    }

    @Test
    void findByUserName_findsNewlyAddedAdmin() {
        Admin newAdmin = new Admin(6000, "jane_admin", "janepass", "Jane", "Smith", "jane@example.com");
        adminDAO.save(newAdmin);

        Optional<Admin> found = adminDAO.findByUserName("jane_admin");

        assertTrue(found.isPresent(), "Should find newly added admin");
        assertEquals(6000, found.get().getId());
        assertEquals("Jane", found.get().getFirstName());
        assertEquals("Smith", found.get().getLastName());
    }

    @Test
    void multipleOperations_workCorrectly() {
        Admin admin1 = new Admin(7000, "user1", "pass1", "User", "One", "user1@example.com");
        Admin admin2 = new Admin(8000, "user2", "pass2", "User", "Two", "user2@example.com");

        adminDAO.save(admin1);
        adminDAO.save(admin2);
        assertEquals(3, adminDAO.findAll().size());

        Admin found = adminDAO.findById(7000);
        assertNotNull(found);
        found.setEmail("updated1@example.com");
        adminDAO.save(found);

        adminDAO.deleteById(8000);
        assertEquals(2, adminDAO.findAll().size());

        Optional<Admin> byUsername = adminDAO.findByUserName("user1");
        assertTrue(byUsername.isPresent());
        assertEquals("updated1@example.com", byUsername.get().getEmail());

        assertNull(adminDAO.findById(8000));
    }
}
