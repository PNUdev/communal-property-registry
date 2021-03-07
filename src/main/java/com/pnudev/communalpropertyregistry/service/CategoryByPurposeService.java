package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeCreateDto;
import com.pnudev.communalpropertyregistry.dto.PagingCategoryByPurposeDto;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeUpdateDto;
import org.springframework.data.domain.Pageable;


public interface CategoryByPurposeService {

    PagingCategoryByPurposeDto findAll(Pageable pageable);

    CategoryByPurpose findById(Long id);

    void create(CategoryByPurposeCreateDto categoryByPurposeDto);

    Long update(CategoryByPurposeUpdateDto categoryByPurposeUpdateDto);

    void delete(Long id);

}
