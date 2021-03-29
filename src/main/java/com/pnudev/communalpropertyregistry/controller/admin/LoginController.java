package com.pnudev.communalpropertyregistry.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.util.Objects.nonNull;

@Controller
@RequestMapping
public class LoginController {

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {

        if (nonNull(error)) {
            model.addAttribute("incorrectDataMessage", "Логін або пароль введено не вірно");
        }

        if (nonNull(logout)){
            model.addAttribute("userLogoutMessage", "Ви успішно вийшли з аккаунту");
        }

        return "admin/login/login";
    }


}
