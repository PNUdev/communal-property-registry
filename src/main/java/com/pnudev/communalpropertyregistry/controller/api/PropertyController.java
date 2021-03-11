package com.pnudev.communalpropertyregistry.controller.api;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService locationService) {
        this.propertyService = locationService;
    }

    @GetMapping
    public Page<PropertyResponseDto> getPropertiesBySearch(
            @PageableDefault Pageable pageable,
            @Nullable @RequestParam(name = "q") String searchQuery,
            @Nullable @RequestParam(name = "status") String propertyStatus,
            @Nullable @RequestParam(name = "category") Long categoryByPurposeId) {

        return propertyService.findPropertiesBySearchQuery(
                searchQuery, propertyStatus, categoryByPurposeId, pageable);
    }

    @GetMapping("/{id}")
    public PropertyResponseDto getPropertyById(@PathVariable Long id) {
        return propertyService.findById(id);
    }

    @GetMapping("/map-locations")
    public PropertiesLocationsResponseDto getMapLocations(@Nullable @RequestParam(name = "q") String searchQuery,
                                                          @Nullable @RequestParam(name = "status") String propertyStatus,
                                                          @Nullable @RequestParam(name = "category") Long categoryByPurposeId) {

        return propertyService.getMapLocations(searchQuery, propertyStatus, categoryByPurposeId);
    }

}

