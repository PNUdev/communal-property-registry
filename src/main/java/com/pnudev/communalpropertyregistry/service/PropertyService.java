package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyService {

    PropertiesLocationsResponseDto getMapLocations(String searchQuery, String propertyStatus, Long categoryByPurposeId);

    Page<PropertyResponseDto> findPropertiesBySearchQuery(String searchQuery, String propertyStatus,
                                                          Long categoryByPurposeId, Pageable pageable);

    Page<PropertyResponseDto> findPropertiesWithAllFieldsBySearchQuery(String searchQuery, String propertyStatus,
                                                                       Long categoryByPurposeId, Pageable pageable);

    PropertyResponseDto findById(Long id);

}
