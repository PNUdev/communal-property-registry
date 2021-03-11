package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeResponseDto;
import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryByPurposeServiceImpl implements CategoryByPurposeService {

    private final CategoryByPurposeRepository categoryByPurposeRepository;

    @Autowired
    public CategoryByPurposeServiceImpl(CategoryByPurposeRepository categoryByPurposeRepository) {
        this.categoryByPurposeRepository = categoryByPurposeRepository;
    }

    @Override
    public CategoryByPurposeResponseDto findAll() {
        return new CategoryByPurposeResponseDto(categoryByPurposeRepository.findAll());
    }

    public CategoryByPurpose findById(Long id) {
        return categoryByPurposeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Дана категорія не існує!"));
    }

}
