package com.smartcv.smartcv.service;

import com.smartcv.smartcv.Dto.RegisterDto;
import com.smartcv.smartcv.model.Users;
import com.smartcv.smartcv.repository.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.Duration;

@Service
public class UsersService {

    @Autowired
    private UsersRepository repository;



    public ModelAndView signUpPage(@ModelAttribute("dtoRegister") RegisterDto dto) {

        ModelAndView view = new ModelAndView("signUp");


        view.addObject("dtoRegister", dto);

        return view;
    }

    public ModelAndView signUp (@Valid @ModelAttribute("dtoRegister") RegisterDto registerDto,  BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView("signUp");

        Users users = registerDto.request();

        if (bindingResult.hasErrors()) {
            System.out.println(users.toString());
            return mv;
        }

        var passwordInvalid = isPasswordInvalid(users);

        var emailValid = emailValid(users);

        if (passwordInvalid) {
            bindingResult.rejectValue("password", "error.dtoRegister", "The password must have between 8 to 20 characters.");
            return mv;
        }

        var emailExiste = repository.findByEmail(users.getEmail());

        if (emailExiste.isPresent()){
            bindingResult.rejectValue("email", "error.dtoRegister", "There is already a user with the email !");
            return mv;
        }

        if (!emailValid){
            bindingResult.rejectValue("email", "error.dtoRegister", "Invalid !");
            return mv;
        }

        if (repository.findByEmailAndPassword(users.getEmail(), users.getPassword()).isEmpty()) {
            try {

                this.repository.save(users);

                request.getSession().setAttribute("username", users.getUsername()); // o nome do usuario foi armazenado na sessão após um cadastro bem sucedido

                Cookie userCookie = new Cookie("username", users.getUsername());
                userCookie.setMaxAge((int) Duration.ofMinutes(5).getSeconds()); // expira em 5 minutos
                userCookie.setPath("/"); // disponível para toda a aplicação
                userCookie.setHttpOnly(true); // significa que o cookie não pode ser acessado via JavaScript no navegador do cliente.
                userCookie.setSecure(true); // isso significa que o cookie só será enviado para o servidor em conexões seguras, ou seja, via HTTPS.
                response.addCookie(userCookie);

              /*  Cookie idCookie = new Cookie("id", String.valueOf(users.getId())); // Corrigir aqui
                idCookie.setMaxAge((int) Duration.ofMinutes(5).getSeconds());
                idCookie.setPath("/");
                idCookie.setHttpOnly(true);
                idCookie.setSecure(true);
                response.addCookie(idCookie);*/

                response.getWriter().println("Atributo armazenado na sessão: " + users.getId());

                return new ModelAndView("redirect:/SmartCV");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred, try it again");
                return null;
            }
        }
        return mv;
    }

    private static boolean isPasswordInvalid(Users users) {
        return users.getPassword().length() <= 7
                || users.getPassword().length() >= 19;
    }

    private static boolean emailValid(Users users) {
        return users.getEmail().contains("@gmail.com")
                || users.getEmail().contains("@outlook.com")
                || users.getEmail().contains("@hotmail.com");
    }
}