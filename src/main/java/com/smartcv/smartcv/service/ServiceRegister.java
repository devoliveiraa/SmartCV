package com.smartcv.smartcv.service;

import com.smartcv.smartcv.config.SecurityConfig;
import com.smartcv.smartcv.dto.Profession;
import com.smartcv.smartcv.dto.RegisterDto;
import com.smartcv.smartcv.model.Users;
import com.smartcv.smartcv.repository.UsersRepository;
import com.smartcv.smartcv.strategy.CookieAttributes;
import com.smartcv.smartcv.strategy.EmailValid;
import com.smartcv.smartcv.strategy.IsInvalidPassword;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Service
public class ServiceRegister {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private EmailValid emailValid;

    @Autowired
    private IsInvalidPassword isInvalidPassword;

    @Autowired
    private CookieAttributes cookieAttributes;

    @Autowired
    private PasswordEncoder securityConfig;


    public ModelAndView signUpPage(@ModelAttribute("dtoRegister") RegisterDto dto) {

        ModelAndView view = new ModelAndView("register");

        view.addObject("dtoRegister", dto);
        view.addObject("listaStatusUser", Profession.values());

        return view;
    }

    public ModelAndView signUp(@Valid @ModelAttribute("dtoRegister") RegisterDto registerDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView("register");

        Users users = registerDto.request();

        var emailInvalid = emailValid.validation(users);

        var emailExiste = repository.findByEmail(users.getEmail());

        var passwordInvalid = isInvalidPassword.validation(users);

        var verificationEmailAndPassword = repository.findByEmailAndPassword(users.getEmail(), users.getPassword());

        var userIsNull = users.getUsername() != null && users.getEmail() != null && users.getPassword() != null && users.getProfession() != null;

        if (bindingResult.hasErrors()) {
            return mv;

        } else if (emailExiste.isPresent()) {
            bindingResult.rejectValue("email", "error.dtoRegister", "There is already a user with the email !");
            return mv;

        } else if (!emailInvalid) {
            bindingResult.rejectValue("email", "error.dtoRegister", "Invalid !");
            return mv;

        } else if (passwordInvalid) {
            bindingResult.rejectValue("password", "error.loginDto", "⬛ Your password need have at least 8 to 20 character");
            bindingResult.rejectValue("password", "error.loginDto", "⬛ Supper and lower case letters and symbols");
            return mv;

        } else {

            if (verificationEmailAndPassword.isEmpty()) {

                if (userIsNull) {

                    try {
                     /*   String encodedPassword = securityConfig.encode(registerDto.getPassword());
                        users.setPassword(encodedPassword);*/
                        this.repository.save(users);

                        request.getSession().setAttribute("username", users.getUsername());
                        request.getSession().setAttribute("id", users.getId());
                        request.getSession().setAttribute("profession", users.getProfession().name());

                        Cookie userCookie = new Cookie("username", users.getUsername());
                        cookieAttributes.setCookieAttributes(userCookie);

                        Cookie idCookie = new Cookie("id", String.valueOf(users.getId()));
                        cookieAttributes.setCookieAttributes(idCookie);

                        Cookie professionCookie = new Cookie("profession", users.getProfession().name());
                        cookieAttributes.setCookieAttributes(professionCookie);

                        response.addCookie(userCookie);
                        response.addCookie(idCookie);
                        response.addCookie(professionCookie);

                        return new ModelAndView("redirect:/SmartCV");

                    } catch (Exception e) {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred, try it again");
                        return null;
                    }

                } else {
                    System.err.println("This user is null, try again later");
                }
            }
        }
        return mv;
    }
}