package com.pnudev.communalpropertyregistry.controller.admin;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.dto.AttachmentAdminDto;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.AttachmentAdminFormDto;
import com.pnudev.communalpropertyregistry.service.AttachmentAdminService;
import com.pnudev.communalpropertyregistry.service.AttachmentCategoryAdminService;
import com.pnudev.communalpropertyregistry.service.PropertyAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.pnudev.communalpropertyregistry.util.FlashMessageConstants.SUCCESS_FLASH_MESSAGE;

@Controller
@RequestMapping("/admin/attachments")
public class AttachmentAdminController {

    private final AttachmentAdminService attachmentAdminService;

    private final PropertyAdminService propertyAdminService;

    private final AttachmentCategoryAdminService attachmentCategoryAdminService;

    @Autowired
    public AttachmentAdminController(AttachmentAdminService attachmentAdminService,
                                     PropertyAdminService propertyAdminService,
                                     AttachmentCategoryAdminService attachmentCategoryAdminService) {

        this.attachmentAdminService = attachmentAdminService;
        this.propertyAdminService = propertyAdminService;
        this.attachmentCategoryAdminService = attachmentCategoryAdminService;
    }

    @GetMapping("/new/property/{id}")
    public String create(Model model) {

        List<AttachmentCategory> attachmentCategories = attachmentCategoryAdminService.findAll();
        model.addAttribute("attachmentCategories", attachmentCategories);

        return "admin/attachment/form";
    }

    @GetMapping("/property/{id}")
    public String find(@PathVariable(name = "id") Long id, Model model) {

        PropertyAdminDto property = propertyAdminService.findById(id);
        List<AttachmentAdminDto> attachments = attachmentAdminService.findAllByPropertyId(id);

        model.addAttribute("property", property);
        model.addAttribute("attachments", attachments);

        return "admin/attachment/index";
    }

    @GetMapping("/update/{attachment_id}/property/{property_id}")
    public String update(@PathVariable(name = "attachment_id") Long attachmentId,
                         @PathVariable(name = "property_id") Long propertyId,
                         Model model) {

        AttachmentAdminDto attachment = attachmentAdminService.findById(attachmentId);
        List<AttachmentCategory> attachmentCategories = attachmentCategoryAdminService.findAll();

        model.addAttribute("attachmentCategories", attachmentCategories);
        model.addAttribute("attachment", attachment);
        model.addAttribute("propertyId", propertyId);

        return "admin/attachment/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirmation(@RequestHeader(value = "referer", defaultValue = "/admin/properties") String returnBackUrl,
                                     Model model) {

        model.addAttribute("message", "Ви впевнені, що хочете видалити прикріплення?");
        model.addAttribute("returnBackUrl", returnBackUrl);

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/new/property/{id}")
    public String create(@PathVariable(name = "id") Long propertyId,
                         AttachmentAdminFormDto attachmentAdminFormDto,
                         RedirectAttributes redirectAttributes) {

        attachmentAdminService.create(propertyId, attachmentAdminFormDto);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Прикріплення успішно створено!");

        return "redirect:/admin/attachments/property/" + propertyId;
    }

    @PostMapping("/update/{attachment_id}/property/{property_id}")
    public String update(@PathVariable(name = "attachment_id") Long attachmentId,
                         @PathVariable(name = "property_id") Long propertyId,
                         AttachmentAdminFormDto attachmentAdminFormDto,
                         RedirectAttributes redirectAttributes) {

        attachmentAdminService.updateById(attachmentId, propertyId, attachmentAdminFormDto);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Прикріплення успішно оновлено!");

        return "redirect:/admin/attachments/property/" + propertyId;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id,
                         RedirectAttributes redirectAttributes) {

        attachmentAdminService.deleteById(id);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Прикріплення успішно видалено!");

        return "redirect:/admin/properties";
    }

}
