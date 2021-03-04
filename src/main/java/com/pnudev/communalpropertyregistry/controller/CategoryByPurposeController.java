package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories-by-purpose")
public class CategoryByPurposeController {

    private CategoryByPurposeService categoryByPurposeService;

    @Autowired
    public CategoryByPurposeController(CategoryByPurposeService categoryByPurposeService) {
        this.categoryByPurposeService = categoryByPurposeService;
    }

    @GetMapping
    public CategoryByPurpose getCategories() {
        return categoryByPurposeService.getCategories();
    }

}
