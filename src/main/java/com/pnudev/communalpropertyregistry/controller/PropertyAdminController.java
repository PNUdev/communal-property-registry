package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminFormDto;
import com.pnudev.communalpropertyregistry.service.CategoryByPurposeService;
import com.pnudev.communalpropertyregistry.service.PropertyAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Controller
@RequestMapping("/admin/properties")
public class PropertyAdminController {

    private final PropertyAdminService propertyAdminService;

    private final CategoryByPurposeService categoryByPurposeService;

    @Autowired
    public PropertyAdminController(PropertyAdminService propertyAdminService,
                                   CategoryByPurposeService categoryByPurposeService) {

        this.propertyAdminService = propertyAdminService;
        this.categoryByPurposeService = categoryByPurposeService;
    }

    @GetMapping
    public String findAll(@Nullable @RequestParam(name = "q") String q,
                          @Nullable @RequestParam(name = "category") Long categoryByPurposeId,
                          @Nullable @RequestParam(name = "status") String propertyStatus,
                          @PageableDefault(sort = "name") Pageable pageable,
                          Model model) {

        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeService.findAll();
        Page<PropertyAdminDto> propertiesAdminPage = propertyAdminService.findAll(
                !Objects.equals(q, "") ? q : null,
                nonNull(categoryByPurposeId) && categoryByPurposeId != -1 ? categoryByPurposeId : null,
                !Objects.equals(propertyStatus, "all") ? propertyStatus : null,
                pageable);

        model.addAttribute("categoriesByPurpose", categoriesByPurpose);
        model.addAttribute("propertiesPage", propertiesAdminPage);

        return "admin/common/index";
    }

    @GetMapping("/new")
    public String create(Model model) {

        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeService.findAll();

        model.addAttribute("categoriesByPurpose", categoriesByPurpose);

        return "admin/common/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {

        PropertyAdminDto propertyAdminDto = propertyAdminService.findById(id);

        model.addAttribute("propertyAdminDto", propertyAdminDto);

        return "admin/common/form";
    }

    @PostMapping("/save")
    public String save(PropertyAdminFormDto propertyAdminFormDto, RedirectAttributes redirectAttributes) {

        propertyAdminService.save(propertyAdminFormDto);

//        redirectAttributes.addFlashAttribute("");

        return "admin/common/index";
    }

}
