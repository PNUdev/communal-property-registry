package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeDto;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposePageDto;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeResponseDto;
import com.pnudev.communalpropertyregistry.dto.form.CategoryByPurposeFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CategoryByPurposeService {

    CategoryByPurposePageDto findAll(Pageable pageable);

    CategoryByPurpose findById(Long id);

    void create(CategoryByPurposeFormDto categoryByPurposeDto);

    void update(CategoryByPurposeFormDto categoryByPurposeDto, Long categoryId);

    void delete(Long id);

}
