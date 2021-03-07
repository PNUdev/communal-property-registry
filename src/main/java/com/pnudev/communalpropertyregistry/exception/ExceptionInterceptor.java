package com.pnudev.communalpropertyregistry.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.pnudev.communalpropertyregistry.util.FlashMessageConstants.ERROR_FLASH_MESSAGE;

@ControllerAdvice
@Slf4j
public class ExceptionInterceptor {

    @ExceptionHandler(ServiceException.class)
    public String serviceException(ServiceException serviceException, RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) {

        log.error("ServiceException was thrown, httpServletRequest: {}", request, serviceException);

        redirectAttributes.addFlashAttribute(ERROR_FLASH_MESSAGE, serviceException.getMessage());

        return "redirect:/";
    }
}
