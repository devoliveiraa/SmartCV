package com.smartcv.smartcv.Dto;

import com.smartcv.smartcv.model.Users;

import java.util.Objects;

public class RegisterDto {

    private String username;

    private String email;

    private String password;

    public RegisterDto() {
    }

    public RegisterDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterDto that = (RegisterDto) o;
        return Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password);
    }

    public Users request (){
        Users users = new Users();
        users.setUsername(this.username);
        users.setEmail(this.email);
        users.setPassword(this.password);
        return users;
    }

}