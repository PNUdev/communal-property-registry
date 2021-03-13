package com.pnudev.communalpropertyregistry.controller.admin;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.form.CategoryByPurposeFormDto;
import com.pnudev.communalpropertyregistry.service.CategoryByPurposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.pnudev.communalpropertyregistry.util.FlashMessageConstants.SUCCESS_FLASH_MESSAGE;

@Controller
@RequestMapping("/admin/categories")
public class CategoryByPurposeAdminController {

    private final CategoryByPurposeService categoryByPurposeService;

    @Autowired
    public CategoryByPurposeAdminController(CategoryByPurposeService categoryByPurposeService) {
        this.categoryByPurposeService = categoryByPurposeService;
    }

    @GetMapping
    public String findAll(@PageableDefault Pageable pageable, Model model) {

        Page<CategoryByPurpose> categoriesPage = categoryByPurposeService
                .findAll(pageable);

        model.addAttribute("categoriesPage", categoriesPage);

        return "admin/category/index";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {

        CategoryByPurpose category = categoryByPurposeService.findById(id);
        model.addAttribute("category", category);

        return "admin/category/show";
    }

    @GetMapping("/new")
    public String createForm() {
        return "admin/category/form";
    }

    @PostMapping("/create")
    public String create(@Validated CategoryByPurposeFormDto categoryByPurposeCreateDto,
                         RedirectAttributes redirectAttributes) {

        categoryByPurposeService.create(categoryByPurposeCreateDto);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(),
                "Категорія створена успішно!");

        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {

        CategoryByPurpose category = categoryByPurposeService.findById(id);
        model.addAttribute("category", category);

        return "admin/category/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long categoryId,
                         @Validated CategoryByPurposeFormDto categoryByPurposeDto,
                         RedirectAttributes redirectAttributes) {

        categoryByPurposeService.update(categoryByPurposeDto, categoryId);
        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(),
                "Категорія оновлена успішно!");

        return "redirect:/admin/categories/" + categoryId;
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirmation(Model model, @RequestHeader("referer") String returnBackUrl) {

        model.addAttribute("message", "Ви впевнені, що хочете видалити дану категорію?");
        model.addAttribute("returnBackUrl", returnBackUrl);

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        categoryByPurposeService.delete(id);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(),
                "Категорія видалена успішно!");

        return "redirect:/admin/categories/";
    }

}
