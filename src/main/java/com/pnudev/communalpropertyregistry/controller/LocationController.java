package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.dto.PropertyLocationResponseDto;
import com.pnudev.communalpropertyregistry.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties/map-locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public PropertyLocationResponseDto getLocation(@Nullable @RequestParam(name = "q") String searchQuery,
                                                   @Nullable @RequestParam(name = "status") String propertyStatus,
                                                   @Nullable @RequestParam(name = "category") Long categoryByPurposeId) {

        return locationService.getLocation(searchQuery, propertyStatus, categoryByPurposeId);
    }

}

