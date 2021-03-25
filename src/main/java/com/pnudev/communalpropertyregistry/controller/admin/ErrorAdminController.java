package com.pnudev.communalpropertyregistry.controller.admin;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorAdminController implements ErrorController {

    @RequestMapping("/error")
    public String showErrorPage(@RequestHeader("referer") String previousLocation, Model model) {

        model.addAttribute("previousLocation", previousLocation);
        model.addAttribute("errorMessage", "Сторінку не знайдено!");

        return "main/error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
