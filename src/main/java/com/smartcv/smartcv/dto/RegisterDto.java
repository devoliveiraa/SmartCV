package com.smartcv.smartcv.dto;

import com.smartcv.smartcv.model.Users;

import java.util.Objects;

public class RegisterDto {

    private String username;

    private String email;

    private String password;

    private Profession profession;

    public RegisterDto() {
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

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Users request (){
        Users users = new Users();
        users.setUsername(this.username);
        users.setEmail(this.email);
        users.setPassword(this.password);
        users.setProfession(this.profession);
        return users;
    }



}