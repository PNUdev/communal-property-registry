package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeResponseDto;

import java.util.List;

public interface CategoryByPurposeService {

    CategoryByPurposeResponseDto findAll();

    List<CategoryByPurpose> finAllCategories();

    CategoryByPurpose findById(Long id);

}
