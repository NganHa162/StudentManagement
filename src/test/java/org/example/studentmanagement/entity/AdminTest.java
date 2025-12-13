package org.example.studentmanagement.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class AdminTest {

    @Test
    void defaultConstructor_createsAdminObject() {
        Admin admin = new Admin();

        assertNotNull(admin, "Default constructor should create an Admin object");
    }

    @Test
    void constructor_setsAllFields() {
        Admin admin = new Admin(1, "admin123", "password123", "John", "Doe", "john.doe@example.com");

        assertEquals(1, admin.getId());
        assertEquals("admin123", admin.getUserName());
        assertEquals("password123", admin.getPassword());
        assertEquals("John", admin.getFirstName());
        assertEquals("Doe", admin.getLastName());
        assertEquals("john.doe@example.com", admin.getEmail());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        Admin admin = new Admin();

        admin.setId(2);
        admin.setUserName("jane_admin");
        admin.setPassword("securePass456");
        admin.setFirstName("Jane");
        admin.setLastName("Smith");
        admin.setEmail("jane.smith@example.com");

        assertEquals(2, admin.getId());
        assertEquals("jane_admin", admin.getUserName());
        assertEquals("securePass456", admin.getPassword());
        assertEquals("Jane", admin.getFirstName());
        assertEquals("Smith", admin.getLastName());
        assertEquals("jane.smith@example.com", admin.getEmail());
    }

    @Test
    void setId_updatesId() {
        Admin admin = new Admin();
        admin.setId(10);

        assertEquals(10, admin.getId());
    }

    @Test
    void setUserName_updatesUserName() {
        Admin admin = new Admin();
        admin.setUserName("testuser");

        assertEquals("testuser", admin.getUserName());
    }

    @Test
    void setPassword_updatesPassword() {
        Admin admin = new Admin();
        admin.setPassword("newPassword");

        assertEquals("newPassword", admin.getPassword());
    }

    @Test
    void setFirstName_updatesFirstName() {
        Admin admin = new Admin();
        admin.setFirstName("Alice");

        assertEquals("Alice", admin.getFirstName());
    }

    @Test
    void setLastName_updatesLastName() {
        Admin admin = new Admin();
        admin.setLastName("Johnson");

        assertEquals("Johnson", admin.getLastName());
    }

    @Test
    void setEmail_updatesEmail() {
        Admin admin = new Admin();
        admin.setEmail("alice.johnson@example.com");

        assertEquals("alice.johnson@example.com", admin.getEmail());
    }

    @Test
    void allFields_canBeSetAndRetrieved() {
        Admin admin = new Admin(100, "superadmin", "pass@word", "Super", "Admin", "super@admin.com");

        assertEquals(100, admin.getId());
        assertEquals("superadmin", admin.getUserName());
        assertEquals("pass@word", admin.getPassword());
        assertEquals("Super", admin.getFirstName());
        assertEquals("Admin", admin.getLastName());
        assertEquals("super@admin.com", admin.getEmail());
    }
}
