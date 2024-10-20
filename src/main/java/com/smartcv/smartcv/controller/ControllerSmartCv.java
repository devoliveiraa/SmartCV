package com.smartcv.smartcv.controller;

import com.smartcv.smartcv.Dto.LoginDto;
import com.smartcv.smartcv.Dto.RegisterDto;
import com.smartcv.smartcv.service.ServiceIndex;
import com.smartcv.smartcv.service.ServiceLogin;
import com.smartcv.smartcv.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/SmartCV")
public class ControllerSmartCv {

    @Autowired
    private UsersService service;

    @Autowired
    private ServiceIndex serviceIndex;
    @Autowired
    private ServiceLogin serviceLogin;

    @GetMapping
    public ModelAndView index (HttpServletRequest request){
        ModelAndView index = new ModelAndView("index");
        return serviceIndex.index(request);
    }

    @GetMapping("/login")
    public ModelAndView login (){
        ModelAndView login = new ModelAndView("login");
        return serviceLogin.login();
    }

    @GetMapping("/signUp")
    public ModelAndView singUp (@ModelAttribute("dtoRegister")RegisterDto dto){
        ModelAndView signUp = new ModelAndView("signUp");
        return service.signUpPage(dto);
    }

    @PostMapping("/signUpRegister")
    public ModelAndView register (@Valid @ModelAttribute("dtoRegister") RegisterDto dtoRegister, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return service.signUp(dtoRegister, bindingResult, request, response);
    }

    @PostMapping("/sendLogin")
    public ModelAndView loginSend (@Valid LoginDto dto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return serviceLogin.sendLogin(dto, bindingResult, request, response);
    }
}