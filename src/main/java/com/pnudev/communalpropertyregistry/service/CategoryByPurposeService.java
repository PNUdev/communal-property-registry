package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.form.CategoryByPurposeFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CategoryByPurposeService {

    Page<CategoryByPurpose> findAll(Pageable pageable);

    CategoryByPurpose findById(Long id);

    void create(CategoryByPurposeFormDto categoryByPurposeDto);

    void updateById(CategoryByPurposeFormDto categoryByPurposeDto, Long categoryId);

    void deleteById(Long id);

}
