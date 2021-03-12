package com.pnudev.communalpropertyregistry.controller.admin;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.dto.AttachmentCategoryDto;
import com.pnudev.communalpropertyregistry.service.AttachmentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.pnudev.communalpropertyregistry.util.FlashMessageConstants.SUCCESS_FLASH_MESSAGE;

@Controller
@RequestMapping("/admin/attachment-categories")
public class AttachmentCategoryAdminController {

    private final AttachmentCategoryService attachmentCategoryService;

    @Autowired
    public AttachmentCategoryAdminController(AttachmentCategoryService attachmentCategoryService) {
        this.attachmentCategoryService = attachmentCategoryService;
    }

    @GetMapping
    public String findAll(@PageableDefault Pageable pageable, Model model) {

        Page<AttachmentCategory> attachmentCategoriesPage = attachmentCategoryService.findAll(pageable);
        model.addAttribute("attachmentCategoriesPage", attachmentCategoriesPage);

        return "admin/attachmentCategory/index";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model){

        model.addAttribute("attachmentCategory", attachmentCategoryService.findById(id));

        return "admin/attachmentCategory/show";
    }

    @GetMapping("/new")
    public String create(){
        return "admin/attachmentCategory/new";
    }

    @PostMapping("/new")
    public String create(AttachmentCategoryDto attachmentCategoryDto){

        attachmentCategoryService.create(attachmentCategoryDto);

        return "redirect:/admin/attachment-categories";
    }

    @GetMapping("/edit/{id}")
    public String editById(@PathVariable Long id, Model model){

        model.addAttribute("attachmentCategory", attachmentCategoryService.findById(id));

        return "admin/attachmentCategory/edit";
    }

    @PostMapping("/edit/{id}")
    public String editById(@PathVariable Long id, AttachmentCategoryDto attachmentCategoryDto){

        attachmentCategoryService.updateById(id, attachmentCategoryDto);

        return "redirect:/admin/attachment-categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id, Model model){

        model.addAttribute("message", "Ви впевнені, що хочете видалити дану категорію?");
        model.addAttribute("returnBackUrl", "/admin/attachment-categories/" + id);

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        attachmentCategoryService.delete(id);
        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Категорію було успішно видалено!");

        return "redirect:/admin/attachment-categories";
    }

}
