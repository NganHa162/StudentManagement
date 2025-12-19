package org.example.studentmanagement.ui;

public class LoginUI {
    private String username;
    private String password;

    public String render() {
        return "login/login-form";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


