package com.smartcv.smartcv.dto;

import com.smartcv.smartcv.model.Users;



public class LoginDto {

    private String username;

    private String email;

    private String password;

    private Profession profession;

    public LoginDto() {
    }

    public LoginDto(String email, String password, Profession profession) {
        this.email = email;
        this.password = password;
        this.profession = profession;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
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

    public Users request (){
        Users users = new Users();
        users.setEmail(this.email);
        users.setPassword(this.password);
        users.setProfession(this.profession);
        return users;
    }
}