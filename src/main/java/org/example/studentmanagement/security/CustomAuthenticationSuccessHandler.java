package org.example.studentmanagement.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.studentmanagement.controller.LoginResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String redirectUrl = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::resolveUrlByRole)
                .findFirst()
                .orElse("/login?error");

        LoginResponse loginResponse = new LoginResponse("Login successful", redirectUrl);
        request.getSession().setAttribute("loginResponse", loginResponse);
        response.sendRedirect(redirectUrl);
    }

    private String resolveUrlByRole(String authority) {
        return switch (authority) {
            case "ROLE_ADMIN" -> "/admin/adminPanel"; // Updated to match AdminController endpoint
            case "ROLE_TEACHER" -> "/teacher/dashboard";
            case "ROLE_STUDENT" -> "/student/dashboard";
            default -> "/login?error";
        };
    }
}


