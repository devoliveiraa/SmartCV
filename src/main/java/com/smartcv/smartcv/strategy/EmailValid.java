package com.smartcv.smartcv.strategy;

import com.smartcv.smartcv.model.Users;
import com.smartcv.smartcv.strategy.impl.ValidationImpl;
import org.springframework.stereotype.Component;

@Component
public class EmailValid implements ValidationImpl {


    @Override
    public boolean validation(Users users) {
        return users.getEmail().contains("@gmail.com")
                || users.getEmail().contains("@hotmail.com")
                || users.getEmail().contains("@protonmail.com")
                || users.getEmail().contains("@zoho.com")
                || users.getEmail().contains("@icloud.com")
                || users.getEmail().contains("@yahoo.com")
                || users.getEmail().contains("@outlook.com");
    }
}