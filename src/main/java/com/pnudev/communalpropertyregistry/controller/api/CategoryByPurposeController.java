package com.pnudev.communalpropertyregistry.controller.api;

import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeResponseDto;
import com.pnudev.communalpropertyregistry.service.CategoryByPurposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories-by-purpose")
public class CategoryByPurposeController {

    private final CategoryByPurposeService categoryByPurposeService;

    @Autowired
    public CategoryByPurposeController(CategoryByPurposeService categoryByPurposeService) {
        this.categoryByPurposeService = categoryByPurposeService;
    }

    @GetMapping
    public CategoryByPurposeResponseDto getCategories() {
        return new CategoryByPurposeResponseDto(categoryByPurposeService.findAll());
    }

}
