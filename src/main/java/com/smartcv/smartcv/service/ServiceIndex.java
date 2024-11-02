package com.smartcv.smartcv.service;

import com.smartcv.smartcv.dto.Profession;
import com.smartcv.smartcv.model.Users;
import com.smartcv.smartcv.repository.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class ServiceIndex {

    @Autowired
    private UsersRepository usersRepository;

    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");

        String username = null;
        Long id = null;
        String profession = null;


        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie Name: " + cookie.getName() + ", Value: " + cookie.getValue());
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue(); // Pega o nome do usuário armazenado no cookie
                } else if ("profession".equals(cookie.getName())) {
                    profession = cookie.getValue();
                }
                if ("id".equals(cookie.getName())) {
                    String cookieValue = cookie.getValue();
                    if (cookieValue != null && !cookieValue.equals("null")) {
                        try {
                            id = Long.parseLong(cookieValue); // Converte e atribui o valor à variável id
                        } catch (NumberFormatException e) {
                            System.err.println("Error converting ID from cookie: " + e.getMessage());
                        }
                    }
                }
            }
        }


        // Se o nome de usuário estiver no cookie, salva na sessão
        System.out.println("---------------- O que foi salvo na sessão -------------------");

        request.getSession().setAttribute("username", username);
        request.getSession().setAttribute("id", id);
        request.getSession().setAttribute("profession", profession);

        System.out.println("Nome de usuário salvo na sessão: " + username);
        System.out.println("Id de usuário salvo na sessão: " + id);
        System.out.println("Profession de usuário salvo na sessão: " + profession);


        String newUser = (String) request.getSession().getAttribute("username");
        Long newUserId = (Long) request.getSession().getAttribute("id");
        Profession newUserProfession = null;

        if (profession != null) {
            try {
                newUserProfession = Profession.valueOf(profession);
            } catch (IllegalArgumentException e) {
                System.out.println("Error to convert to profession: " + e.getMessage());
            }
        }


        // Verifica se o nome de usuário foi encontrado e adiciona ao modelAndView
        if (newUser != null && newUserId != null) {
            modelAndView.addObject("newUsername", newUser); // Passa o nome do usuário para a view
            modelAndView.addObject("newUsernameId", newUserId);
            modelAndView.addObject("newUserProfession", newUserProfession);
        }
        return modelAndView;
    }
}