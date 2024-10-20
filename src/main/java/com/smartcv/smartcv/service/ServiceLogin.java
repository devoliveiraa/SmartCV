package com.smartcv.smartcv.service;

import com.smartcv.smartcv.Dto.LoginDto;
import com.smartcv.smartcv.model.Users;
import com.smartcv.smartcv.repository.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.Duration;

@Service
public class ServiceLogin {


    @Autowired
    private UsersRepository repository;
    
    
    
    public ModelAndView login (){
        ModelAndView modelAndView = new ModelAndView("login");

        LoginDto dto = new LoginDto();

        modelAndView.addObject("loginDto", dto);

        return modelAndView;
    }
    

    public ModelAndView sendLogin (@Valid LoginDto dto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView("login");

        Users users = dto.request();

        if (bindingResult.hasErrors()) {
            System.out.println(users.toString());
            System.err.println("Bindig um");
            return mv;
        }

        var passwordInvalid = isPasswordInvalid(users);

        var emailValid = emailValid(users);

        var verificationUserAndPassword = repository.findByEmailAndPassword(users.getEmail(), users.getPassword());


        if (passwordInvalid) {
            bindingResult.rejectValue("password", "error.loginDto", "The password must have between 8 to 20 characters.");
            System.err.println("Bindig 2");
            return mv;
        }

        if (!emailValid){
            bindingResult.rejectValue("email", "error.loginDto", "Invalid !");
            System.err.println("Bindig 3");
            return mv;
        }

        if (verificationUserAndPassword.isEmpty()) {
            bindingResult.rejectValue("password", "error.loginDto", "Email or password.");
            System.err.println("Bindig 4");
            return mv;
        }

        try {
            System.out.println("Deu certo");

            Users user = verificationUserAndPassword.get();

            request.getSession().setAttribute("email", users.getEmail()); // o nome do usuario foi armazenado na sessão após um cadastro bem sucedido


            Cookie userCookie = new Cookie("email", users.getEmail());
            userCookie.setMaxAge((int) Duration.ofMinutes(5).getSeconds()); // expira em 5 minutos
            userCookie.setPath("/"); // disponível para toda a aplicação
            userCookie.setHttpOnly(true); // significa que o cookie não pode ser acessado via JavaScript no navegador do cliente.
            userCookie.setSecure(true); // isso significa que o cookie só será enviado para o servidor em conexões seguras, ou seja, via HTTPS.
            response.addCookie(userCookie);


            response.getWriter().println("Atributo armazenado na sessão: " + users.getId());

            return new ModelAndView("redirect:/SmartCV");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred, try it again");
            return null;
        }
    }

    private static boolean emailValid(Users users) {
        return users.getEmail().contains("@gmail.com")
                || users.getEmail().contains("@outlook.com")
                || users.getEmail().contains("@hotmail.com");
    }

    private static boolean isPasswordInvalid(Users users) {
        return users.getPassword().length() <= 7
                || users.getPassword().length() >= 19;
    }

}
