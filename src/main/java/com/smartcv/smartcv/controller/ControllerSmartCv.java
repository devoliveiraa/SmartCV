package com.smartcv.smartcv.controller;

import com.smartcv.smartcv.dto.LoginDto;
import com.smartcv.smartcv.dto.PerfilDto;
import com.smartcv.smartcv.dto.RegisterDto;
import com.smartcv.smartcv.service.ServiceIndex;
import com.smartcv.smartcv.service.ServiceLogin;
import com.smartcv.smartcv.service.ServicePerfil;
import com.smartcv.smartcv.service.ServiceRegister;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/SmartCV")
public class ControllerSmartCv {

    @Autowired
    private ServiceRegister service;

    @Autowired
    private ServiceIndex serviceIndex;

    @Autowired
    private ServiceLogin serviceLogin;

    @Autowired
    private ServicePerfil servicePerfil;

    @GetMapping
    public ModelAndView index (HttpServletRequest request){
        return serviceIndex.index(request);
    }

    @GetMapping("/login")
    public ModelAndView login (@ModelAttribute("loginDto") LoginDto loginDto){
        return serviceLogin.login(loginDto);
    }

    @GetMapping("/register")
    public ModelAndView singUp (@ModelAttribute("dtoRegister")RegisterDto dto){
        return service.signUpPage(dto);
    }

    @GetMapping("/profilej")
    public ModelAndView perfil () {
        return servicePerfil.page();
    }

    @PostMapping("/register")
    public ModelAndView register (@Valid @ModelAttribute("dtoRegister") RegisterDto dtoRegister, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return service.signUp(dtoRegister, bindingResult, request, response);
    }

    @PostMapping("/login")
    public ModelAndView loginSend (@Valid @ModelAttribute("loginDto") LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return serviceLogin.sendLogin(loginDto, bindingResult, request, response);
    }

    @GetMapping("/profile")
    public ModelAndView perfil (@RequestParam(name = "id") String id , @ModelAttribute("perfilDto") PerfilDto perfilDto, HttpServletRequest request){
            return servicePerfil.pageAndInfo(id, perfilDto, request);
    }

    @PostMapping("/profileEdit")
    public ModelAndView update (@Valid @ModelAttribute("perfilDto") PerfilDto perfilDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException{
        return servicePerfil.update(perfilDto, bindingResult, request, response);
    }
}