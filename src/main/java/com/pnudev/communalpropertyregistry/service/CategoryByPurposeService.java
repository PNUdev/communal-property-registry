package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.form.CategoryByPurposeFormDto;

import java.util.List;

public interface CategoryByPurposeService {

    List<CategoryByPurpose> findAll();

    CategoryByPurpose findById(Long id);

    void create(CategoryByPurposeFormDto categoryByPurposeDto);

    void updateById(CategoryByPurposeFormDto categoryByPurposeDto, Long categoryId);

    void deleteById(Long id);

}
