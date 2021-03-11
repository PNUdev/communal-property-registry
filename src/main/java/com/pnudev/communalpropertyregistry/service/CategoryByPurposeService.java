package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeResponseDto;

public interface CategoryByPurposeService {

    CategoryByPurposeResponseDto findAll();

    CategoryByPurpose findById(Long id);

}
