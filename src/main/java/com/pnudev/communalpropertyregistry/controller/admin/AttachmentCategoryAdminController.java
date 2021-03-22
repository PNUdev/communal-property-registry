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
    public String createForm(){
        return "admin/attachmentCategory/form";
    }

    @PostMapping("/new")
    public String create(AttachmentCategoryDto attachmentCategoryDto, RedirectAttributes redirectAttributes){

        attachmentCategoryService.create(attachmentCategoryDto);
        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Категорію успішно створено!");

        return "redirect:/admin/attachment-categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){

        model.addAttribute("attachmentCategory", attachmentCategoryService.findById(id));

        return "admin/attachmentCategory/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, AttachmentCategoryDto attachmentCategoryDto,
                           RedirectAttributes redirectAttributes){

        attachmentCategoryService.updateById(id, attachmentCategoryDto);
        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Категорію успішно оновлено!");

        return "redirect:/admin/attachment-categories";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeletion(@PathVariable Long id, Model model){

        model.addAttribute("message", "Ви впевнені, що хочете видалити дану категорію?");
        model.addAttribute("returnBackUrl", "/admin/attachment-categories/" + id);

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        attachmentCategoryService.deleteById(id);
        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Категорію успішно видалено!");

        return "redirect:/admin/attachment-categories";
    }

}
