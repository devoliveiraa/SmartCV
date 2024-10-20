package com.smartcv.smartcv.Dto;

import com.smartcv.smartcv.model.Users;

import java.util.Objects;

public class LoginDto {

    private String email;

    private String password;

    public LoginDto() {
    }

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
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
        return users;
    }

}

