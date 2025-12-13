package org.example.studentmanagement.service;

import java.util.Optional;

import org.example.studentmanagement.dao.AdminDAO;
import org.example.studentmanagement.entity.Admin;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class AdminServiceTest {

    private AdminServiceImpl adminService;
    private AdminDAO adminDAO;

    @BeforeEach
    void setUp() {
        adminDAO = mock(AdminDAO.class);
        adminService = new AdminServiceImpl(adminDAO);
    }

    @Test
    void findByUserName_returnsAdminWhenExists() {
        // Arrange
        Admin admin = new Admin(1000, "admin", "password123", "Admin", "System", "admin@example.com");
        when(adminDAO.findByUserName("admin")).thenReturn(Optional.of(admin));

        // Act
        Optional<Admin> result = adminService.findByUserName("admin");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1000, result.get().getId());
        assertEquals("admin", result.get().getUserName());
        verify(adminDAO).findByUserName("admin");
    }

    @Test
    void findByUserName_returnsEmptyWhenNotFound() {
        // Arrange
        when(adminDAO.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // Act
        Optional<Admin> result = adminService.findByUserName("nonexistent");

        // Assert
        assertTrue(result.isEmpty());
        verify(adminDAO).findByUserName("nonexistent");
    }

    @Test
    void loadUserByUsername_returnsUserDetailsWhenAdminExists() {
        // Arrange
        Admin admin = new Admin(1000, "admin", "encoded_password", "Admin", "System", "admin@example.com");
        when(adminDAO.findByUserName("admin")).thenReturn(Optional.of(admin));

        // Act
        UserDetails userDetails = adminService.loadUserByUsername("admin");

        // Assert
        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        verify(adminDAO).findByUserName("admin");
    }

    @Test
    void loadUserByUsername_throwsUsernameNotFoundExceptionWhenAdminNotFound() {
        // Arrange
        when(adminDAO.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> adminService.loadUserByUsername("nonexistent")
        );

        assertEquals("Admin not found: nonexistent", exception.getMessage());
        verify(adminDAO).findByUserName("nonexistent");
    }

    @Test
    void loadUserByUsername_createsUserWithCorrectRole() {
        // Arrange
        Admin admin = new Admin(2000, "testadmin", "testpass", "Test", "Admin", "test@example.com");
        when(adminDAO.findByUserName("testadmin")).thenReturn(Optional.of(admin));

        // Act
        UserDetails userDetails = adminService.loadUserByUsername("testadmin");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testadmin", userDetails.getUsername());
        assertEquals("testpass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    void findByUserName_delegatesToDAO() {
        // Arrange
        Admin admin = new Admin(1000, "admin", "pass", "Admin", "System", "admin@example.com");
        when(adminDAO.findByUserName(anyString())).thenReturn(Optional.of(admin));

        // Act
        adminService.findByUserName("admin");

        // Assert
        verify(adminDAO).findByUserName("admin");
    }
}

