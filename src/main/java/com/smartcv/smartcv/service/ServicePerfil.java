package com.smartcv.smartcv.service;


import com.smartcv.smartcv.dto.PerfilDto;
import com.smartcv.smartcv.dto.Profession;
import com.smartcv.smartcv.dto.RegisterDto;
import com.smartcv.smartcv.dto.converter.ProfessionConverter;
import com.smartcv.smartcv.model.Users;
import com.smartcv.smartcv.repository.UsersRepository;
import com.smartcv.smartcv.strategy.CookieAttributes;
import com.smartcv.smartcv.strategy.EmailValid;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Service
public class ServicePerfil {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private EmailValid emailValid;

    @Autowired
    private final ProfessionConverter professionConverter;

    @Autowired
    private CookieAttributes cookieAttributes;

    public ServicePerfil(ProfessionConverter professionConverter) {
        this.professionConverter = professionConverter;
    }

    public ModelAndView page() {
        ModelAndView mv = new ModelAndView("profile");
        RegisterDto dto = new RegisterDto();

        mv.addObject("perfilDto", dto);
        mv.addObject("listaStatusUser", Profession.values());

        return mv;
    }

    public ModelAndView pageAndInfo(@RequestParam(name = "id") String id, @ModelAttribute("perfilDto") PerfilDto perfilDto, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("profile");

        var optionalCadastro = repository.findById(id);

        String newUsernameId = (String) request.getSession().getAttribute("id");
        String userIdFromSession = (String) request.getSession().getAttribute("id");
        String newUsername = (String) request.getSession().getAttribute("username");
        String newUsernameProfession = (String) request.getSession().getAttribute("profession");

        var infoForUserNull = newUsernameId == null && newUsername == null && newUsernameProfession == null;

        if (infoForUserNull) {
            System.err.println("Usuário não está logado. Redirecionando para a página inicial.");
            return new ModelAndView("redirect:/SmartCV");
        }

        if (newUsernameId == null || !userIdFromSession.equals(id)) {
            System.err.println("User is not logged in. Redirecting to the home page.");
            return new ModelAndView("redirect:/SmartCV/login");
        }

        if (optionalCadastro.isPresent()) {

            Users user = optionalCadastro.get();

            perfilDto.fromDtoCadastro(user);

            mv.addObject("perfilDto", perfilDto);
            mv.addObject("newUsernameId", newUsernameId);
            mv.addObject("newUsername", newUsername);
            mv.addObject("newUsernameProfession", newUsernameProfession);

        } else {
            System.err.println("Id não encontrado no banco");
        }
        return mv;
    }

    public ModelAndView update(@Valid @ModelAttribute("perfilDto") PerfilDto dto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView("profile");

        Users users = dto.request();

        var invalidEmail = emailValid.validation(users);

        var userOpt = repository.findByEmailAndUsername(users.getEmail(), users.getUsername());

        mv.addObject("perfilDto", dto);

        if (bindingResult.hasErrors()) {
            System.err.println("There was a bindingResult error");
            return mv;
        }

        if (!invalidEmail) {
            bindingResult.rejectValue("email", "error.perfilDto", "Invalid email!");
            return mv;
        }

        if (userOpt.isPresent()) {

            Users user = userOpt.get();

            professionConverter.convert(user.getProfession().name());

            if (repository.findByEmail(users.getEmail()).equals(users.getEmail())) {
                bindingResult.rejectValue("username", "error.dtoPerfil", "The email already exists");
                return mv;
            }

            boolean update = false;

            if (!user.getUsername().equals(users.getUsername())) {
                user.setUsername(users.getUsername());
                update = true;
            }
            if (!user.getEmail().equals(users.getEmail())) {
                user.setEmail(users.getEmail());
                update = true;
            }

            if (update) {
                repository.save(user);

                request.getSession().setAttribute("username", user.getUsername());
                request.getSession().setAttribute("id", user.getId());
                request.getSession().setAttribute("profession", user.getProfession());

                Cookie userCookie = new Cookie("username", user.getUsername());
                cookieAttributes.setCookieAttributes(userCookie);

                Cookie professionCookie = new Cookie("profession", user.getProfession().name());
                cookieAttributes.setCookieAttributes(professionCookie);

                Cookie idCookie = new Cookie("id", user.getId());
                cookieAttributes.setCookieAttributes(idCookie);

                response.addCookie(idCookie);
                response.addCookie(professionCookie);
                response.addCookie(userCookie);

                System.out.println("Information updated successfully");
            }
        }
        return new ModelAndView("redirect:/SmartCV");
    }


}