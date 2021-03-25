package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeResponseDto;
import com.pnudev.communalpropertyregistry.dto.form.CategoryByPurposeFormDto;

public interface CategoryByPurposeService {

    CategoryByPurposeResponseDto findAll();

    CategoryByPurpose findById(Long id);

    void create(CategoryByPurposeFormDto categoryByPurposeDto);

    void updateById(CategoryByPurposeFormDto categoryByPurposeDto, Long categoryId);

    void deleteById(Long id);

}
