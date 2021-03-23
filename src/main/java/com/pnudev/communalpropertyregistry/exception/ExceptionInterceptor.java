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

    @ExceptionHandler(AttachmentAdminException.class)
    public String handleAttachmentAdminException(AttachmentAdminException attachmentAdminException, RedirectAttributes redirectAttributes) {

        log.error("AttachmentAdminException was thrown with propertyId[{}]",
                attachmentAdminException.getPropertyId(),  attachmentAdminException);

        redirectAttributes.addFlashAttribute(ERROR_FLASH_MESSAGE.name(), attachmentAdminException.getMessage());

        return "redirect:/admin/properties/" + attachmentAdminException.getPropertyId() + "/attachments";
    }

    @ExceptionHandler(PropertyAdminException.class)
    public String propertyAdminException(PropertyAdminException propertyAdminException, RedirectAttributes redirectAttributes) {

        log.error("PropertyAdminException was thrown", propertyAdminException);

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

    @ExceptionHandler(AttachmentCategoryException.class)
    public String attachmentCategoryException(AttachmentCategoryException attachmentCategoryException,
                                              RedirectAttributes redirectAttributes,
                                              HttpServletRequest request) {

        log.error("AttachmentCategoryException was thrown, httpServletRequest: {}", request, attachmentCategoryException);

        redirectAttributes.addFlashAttribute(ERROR_FLASH_MESSAGE.name(), attachmentCategoryException.getMessage());

        return "redirect:/admin/attachment-categories";
    }

}

