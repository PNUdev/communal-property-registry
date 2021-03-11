package com.pnudev.communalpropertyregistry.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

}
