package com.pnudev.communalpropertyregistry.controller.admin;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.dto.AttachmentAdminDto;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.AttachmentAdminFormDto;
import com.pnudev.communalpropertyregistry.service.AttachmentAdminService;
import com.pnudev.communalpropertyregistry.service.AttachmentCategoryService;
import com.pnudev.communalpropertyregistry.service.PropertyAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.pnudev.communalpropertyregistry.util.FlashMessageConstants.SUCCESS_FLASH_MESSAGE;

@Controller
@RequestMapping("/admin/properties/{property_id}/attachments")
public class AttachmentAdminController {

    private static final String REDIRECT_URL = "redirect:/admin/properties/%d/attachments";

    private final AttachmentAdminService attachmentAdminService;

    private final PropertyAdminService propertyAdminService;

    private final AttachmentCategoryService attachmentCategoryService;

    @Autowired
    public AttachmentAdminController(AttachmentAdminService attachmentAdminService,
                                     PropertyAdminService propertyAdminService,
                                     AttachmentCategoryService attachmentCategoryService) {

        this.attachmentAdminService = attachmentAdminService;
        this.propertyAdminService = propertyAdminService;
        this.attachmentCategoryService = attachmentCategoryService;
    }

    @GetMapping
    public String findAllByPropertyId(@PathVariable(name = "property_id") Long propertyId, Model model) {

        PropertyAdminDto property = propertyAdminService.findById(propertyId);
        List<AttachmentAdminDto> attachments = attachmentAdminService.findAllByPropertyId(propertyId);

        model.addAttribute("property", property);
        model.addAttribute("attachments", attachments);

        return "admin/attachment/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {

        List<AttachmentCategory> attachmentCategories = attachmentCategoryService.findAll();
        model.addAttribute("attachmentCategories", attachmentCategories);

        return "admin/attachment/form";
    }

    @GetMapping("/edit/{attachment_id}")
    public String editForm(@PathVariable(name = "attachment_id") Long attachmentId,
                           @PathVariable(name = "property_id") Long propertyId,
                           Model model) {

        AttachmentAdminDto attachment = attachmentAdminService.findById(attachmentId, propertyId);
        List<AttachmentCategory> attachmentCategories = attachmentCategoryService.findAll();

        model.addAttribute("attachmentCategories", attachmentCategories);
        model.addAttribute("attachment", attachment);
        model.addAttribute("propertyId", propertyId);

        return "admin/attachment/form";
    }

    @GetMapping("/delete/{attachment_id}")
    public String deleteConfirmation(@PathVariable(name = "property_id") Long propertyId,
                                     Model model) {

        model.addAttribute("message", "Ви впевнені, що хочете видалити прикріплення?");
        model.addAttribute("returnBackUrl", "/admin/properties/" + propertyId + "/attachments");

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/new")
    public String create(@PathVariable(name = "property_id") Long propertyId,
                         AttachmentAdminFormDto attachmentAdminFormDto,
                         RedirectAttributes redirectAttributes) {

        attachmentAdminService.create(propertyId, attachmentAdminFormDto);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Прикріплення успішно створено!");

        return String.format(REDIRECT_URL, propertyId);
    }

    @PostMapping("/update/{attachment_id}")
    public String update(@PathVariable(name = "attachment_id") Long attachmentId,
                         @PathVariable(name = "property_id") Long propertyId,
                         AttachmentAdminFormDto attachmentAdminFormDto,
                         RedirectAttributes redirectAttributes) {

        attachmentAdminService.updateById(attachmentId, propertyId, attachmentAdminFormDto);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Прикріплення успішно оновлено!");

        return String.format(REDIRECT_URL, propertyId);
    }

    @PostMapping("/delete/{attachment_id}")
    public String delete(@PathVariable(name = "property_id") Long propertyId,
                         @PathVariable(name = "attachment_id") Long attachmentId,
                         RedirectAttributes redirectAttributes) {

        attachmentAdminService.deleteById(propertyId, attachmentId);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Прикріплення успішно видалено!");

        return String.format(REDIRECT_URL, propertyId);
    }

}
