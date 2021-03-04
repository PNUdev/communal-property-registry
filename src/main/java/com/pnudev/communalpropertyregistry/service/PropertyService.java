package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

public interface PropertyService {

    Page<Property> findAll(@Nullable String nameOrAddress, @Nullable Long categoryByPurposeId, @Nullable String propertyStatus, Pageable pageable);

}
