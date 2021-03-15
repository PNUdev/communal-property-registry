package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.AddressDto;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.PropertyAdminFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertyAdminService {

    Page<PropertyAdminDto> findAll(String nameOrAddress, Long categoryByPurposeId, String propertyStatus, Pageable pageable);

    PropertyAdminDto findById(Long id);

    void save(PropertyAdminFormDto propertyAdminFormDto, AddressDto address);

    void update(Long id, PropertyAdminFormDto propertyAdminFormDto, AddressDto address);

    List<AddressDto> getAddresses(String address);

    void delete(Long id);

}
