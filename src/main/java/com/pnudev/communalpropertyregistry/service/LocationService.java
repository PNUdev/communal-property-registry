package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.PropertyLocationResponseDto;

public interface LocationService {

    PropertyLocationResponseDto getLocation(String searchQuery, String propertyStatus,  Long categoryByPurposeId);

}
