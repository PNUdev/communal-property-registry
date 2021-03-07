package com.pnudev.communalpropertyregistry.controller.admin;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeDto;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposePageDto;
import com.pnudev.communalpropertyregistry.service.CategoryByPurposeService;
import com.pnudev.communalpropertyregistry.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.pnudev.communalpropertyregistry.util.FlashMessageConstants.SUCCESS_FLASH_MESSAGE;
import static java.lang.Math.abs;
import static java.util.Objects.nonNull;

@Controller
@RequestMapping("/admin/categories")
public class CategoryByPurposeAdminController {

    @Value("${rest.categories.pagination.size}")
    private Integer pageSize;

    private final CategoryByPurposeService categoryByPurposeService;

    @Autowired
    public CategoryByPurposeAdminController(CategoryByPurposeService categoryByPurposeService) {
        this.categoryByPurposeService = categoryByPurposeService;
    }

    @GetMapping
    public String getAllCategories(Integer page, Model model) {

        page = (nonNull(page)) ? page : 0;

        CategoryByPurposePageDto pagination = categoryByPurposeService
                .findAll(PageRequest.of(abs(page), pageSize));

        model.addAttribute("pagination", pagination);

        return "admin/category/index";
    }

    @GetMapping("/{id}")
    public String getCategoryById(@PathVariable("id") Long id, Model model) {

        CategoryByPurpose category = categoryByPurposeService.findById(id);
        model.addAttribute("category", category);

        return "admin/category/show";
    }

    @GetMapping("/new")
    public String getCreatePage(Model model) {

        model.addAttribute("actionType", "Create");
        model.addAttribute("returnBackUrl", "/admin/categories");
        model.addAttribute("postUrl", "/admin/categories/create");

        return "admin/category/form";
    }

    @PostMapping("/create")
    public String create(@Validated CategoryByPurposeDto categoryByPurposeCreateDto,
                         RedirectAttributes redirectAttributes) {

        categoryByPurposeService.create(categoryByPurposeCreateDto);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(),
                "Категорія була успішно створена");

        return "redirect:/admin/categories/new";
    }

    @GetMapping("/edit/{id}")
    public String getUpdatePage(@PathVariable("id") Long id, Model model) {

        CategoryByPurpose category = categoryByPurposeService.findById(id);

        model.addAttribute("actionType", "Update");
        model.addAttribute("returnBackUrl", "/admin/categories/" + id);
        model.addAttribute("postUrl", "/admin/categories/update/" + id);

        model.addAttribute("category", category);

        return "admin/category/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long categoryId,
                         @Validated CategoryByPurposeDto categoryByPurposeDto,
                         RedirectAttributes redirectAttributes) {

        categoryByPurposeService.update(categoryByPurposeDto, categoryId);
        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(),
                "Категорія була успішно оновлена");

        return "redirect:/admin/categories/" + categoryId;
    }

    @GetMapping("/delete/{id}")
    public String getConfirmationPage(@PathVariable("id") Long id, Model model,
                                      HttpServletRequest request) {

        model.addAttribute("message", "Ви впевнені що хочете видалити дану категорію за призначенням?");
        model.addAttribute("returnBackUrl", HttpUtils.getPreviousPageUrl(request));

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        categoryByPurposeService.delete(id);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(),
                "Категорія була успішно видалена");

        return "redirect:/admin/categories/";
    }

}
