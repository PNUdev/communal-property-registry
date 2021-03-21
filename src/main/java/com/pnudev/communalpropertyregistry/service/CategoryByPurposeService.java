package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;

import java.util.List;

public interface CategoryByPurposeService {

    List<CategoryByPurpose> findAll();

    CategoryByPurpose findById(Long id);

}
