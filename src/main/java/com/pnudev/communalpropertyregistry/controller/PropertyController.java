package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/properties")
public class PropertyController {

    @Value("${rest.properties.pagination.size}")
    private Integer pageSize;

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public Page<PropertyResponseDto> getPropertiesBySearch(
            @RequestParam(defaultValue = "0", name = "page") Integer page,
            @Nullable @RequestParam(name = "q") String searchQuery,
            @Nullable @RequestParam(name = "status") String propertyStatus,
            @Nullable @RequestParam(name = "category") Long categoryByPurposeId) {

        return propertyService.findPropertiesBySearchQuery(
                searchQuery, propertyStatus,
                categoryByPurposeId,
                PageRequest.of(Math.abs(page), pageSize));
    }

    @GetMapping("/{id}")
    public PropertyResponseDto getPropertyById(@PathVariable Long id) {
        return propertyService.findById(id);
    }

}
