package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.AddressDto;
import com.pnudev.communalpropertyregistry.dto.AddressResponseDto;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.PropertyAdminFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

public interface PropertyAdminService {

    Page<PropertyAdminDto> findAll(@Nullable String nameOrAddress, @Nullable Long categoryByPurposeId, @Nullable String propertyStatus, Pageable pageable);

    PropertyAdminDto findById(Long id);

    void save(PropertyAdminFormDto propertyAdminFormDto, AddressDto address);

    AddressResponseDto getAddresses(String address);

    void delete(Long id);

}
