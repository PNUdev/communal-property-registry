package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyService {

    PropertiesLocationsResponseDto getMapLocations(String searchQuery, String propertyStatus, Long categoryByPurposeId);

    Page<PropertyResponseDto> findPropertiesBySearchQuery(String searchQuery, String propertyStatus,
                                                          Long categoryByPurposeId, Pageable pageable);

    Property findById(Long id);

    void deleteById(Long id);

}
