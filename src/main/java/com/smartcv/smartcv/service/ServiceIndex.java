package com.smartcv.smartcv.service;

import com.smartcv.smartcv.repository.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class ServiceIndex {


    @Autowired
    private UsersRepository repository;


    public ModelAndView index (HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("index");

        // Recuperar dados dos cookies
        String usuario = null;
        Long id = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    usuario = cookie.getValue();
                }
                if ("id".equals(cookie.getName())) {
                    try {
                        id = Long.parseLong(cookie.getValue());
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter ID do cookie: " + e.getMessage());
                    }
                }
            }
        }

        if (usuario != null && id != null) {
            request.getSession().setAttribute("username", usuario);
            request.getSession().setAttribute("id", id);
        }

        // Usar getSession().getAttribute() em vez de request.getAttribute()
        String usuarioLogado = (String) request.getSession().getAttribute("username"); // pega a sessão e o atributo setado anteriormente

        modelAndView.addObject("username", usuarioLogado);

        return modelAndView;

    }

}
