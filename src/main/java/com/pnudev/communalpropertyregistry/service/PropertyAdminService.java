package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.AddressDto;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.PropertyAdminFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyAdminService {

    Page<PropertyAdminDto> findAll(String nameOrAddress, Long categoryByPurposeId, String propertyStatus, Pageable pageable);

    PropertyAdminDto findById(Long id);

    void create(PropertyAdminFormDto propertyAdminFormDto, AddressDto address);

    void updateById(Long id, PropertyAdminFormDto propertyAdminFormDto, AddressDto address);

    void deleteById(Long id);

}
