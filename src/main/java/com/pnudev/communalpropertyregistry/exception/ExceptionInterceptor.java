package com.pnudev.communalpropertyregistry.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.pnudev.communalpropertyregistry.util.FlashMessageConstants.ERROR_FLASH_MESSAGE;

@Slf4j
@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(PropertyAdminException.class)
    public String propertyAdminException(PropertyAdminException propertyAdminException, RedirectAttributes redirectAttributes) {

        log.trace("PropertyAdminException was thrown", propertyAdminException);

        redirectAttributes.addFlashAttribute(ERROR_FLASH_MESSAGE.name(), propertyAdminException.getMessage());

        return "redirect:/admin/properties";
    }

    @ExceptionHandler(ServiceAdminException.class)
    public String serviceAdminException(ServiceAdminException serviceAdminException, RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) {

        log.error("ServiceAdminException was thrown, httpServletRequest: {}", request, serviceAdminException);

        redirectAttributes.addFlashAttribute(ERROR_FLASH_MESSAGE.name(), serviceAdminException.getMessage());

        return "redirect:/admin";
    }

}