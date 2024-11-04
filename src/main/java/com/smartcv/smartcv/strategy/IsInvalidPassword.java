package com.smartcv.smartcv.strategy;

import com.smartcv.smartcv.model.Users;
import com.smartcv.smartcv.strategy.impl.ValidationImpl;
import org.springframework.stereotype.Component;

@Component
public class IsInvalidPassword implements ValidationImpl {


    @Override
    public boolean validation(Users users) {
        return !users.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*]).{2,20}$");

    }
}