package com.pnudev.communalpropertyregistry.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AdminExceptionInterceptor {

    @ExceptionHandler(ServiceAdminException.class)
    public String serviceAdminException(ServiceAdminException serviceAdminException,
                                        HttpServletRequest request,
                                        Model model) {

        model.addAttribute("errorMessage", serviceAdminException.getMessage());
        model.addAttribute("previousLocation", request.getHeader("referer"));
        return "main/error";
    }

    @ExceptionHandler(CategoryDuplicationException.class)
    public String categoryDuplicationException(HttpServletRequest request,
                                               CategoryDuplicationException categoryDuplicationException,
                                               RedirectAttributes redirectAttributes) {

        String redirectUrl = request.getHeader("referer").split("\\?")[0];
        redirectAttributes.addFlashAttribute("message", categoryDuplicationException.getMessage());

        return "redirect:" + redirectUrl;
    }

}
