package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService locationService;

    @Autowired
    public PropertyController(PropertyService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/map-locations")
    public PropertiesLocationsResponseDto getMapLocations(@Nullable @RequestParam(name = "q") String searchQuery,
                                                          @Nullable @RequestParam(name = "status") String propertyStatus,
                                                          @Nullable @RequestParam(name = "category") Long categoryByPurposeId) {

        return locationService.getMapLocations(searchQuery, propertyStatus, categoryByPurposeId);
    }

}

