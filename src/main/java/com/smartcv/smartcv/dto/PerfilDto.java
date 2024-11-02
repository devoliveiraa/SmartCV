package com.smartcv.smartcv.dto;

import com.smartcv.smartcv.model.Users;

public class PerfilDto {

    private String username;

    private String email;

    private Profession profession;

    public PerfilDto() {
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

    public Users request (){
        Users users = new Users();
        users.setUsername(this.username);
        users.setEmail(this.email);
        users.setProfession(this.profession);
        return users;
    }

    public void fromDtoCadastro(Users user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profession = user.getProfession();
    }
}
