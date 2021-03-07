package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.service.PropertyService;
import com.pnudev.communalpropertyregistry.util.mapper.PropertyToPropertyResponseDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static java.util.Objects.*;

@RestController
@RequestMapping(value = "/api/property", produces = MediaType.APPLICATION_JSON_VALUE)
public class PropertyController {

    private final PropertyService propertyService;
    private final PropertyToPropertyResponseDtoMapper propertyResponseDtoMapper;

    @Autowired
    public PropertyController(PropertyService propertyService,
                              PropertyToPropertyResponseDtoMapper propertyResponseDtoMapper) {

        this.propertyService = propertyService;
        this.propertyResponseDtoMapper = propertyResponseDtoMapper;
    }

    @GetMapping
    public Page<PropertyResponseDto> getPropertiesBySearch(
            @PageableDefault(size = 5, sort = "name") Pageable pageable,
            @Nullable @RequestParam(name = "p") Integer page,
            @Nullable @RequestParam(name = "q") String searchQuery,
            @Nullable @RequestParam(name = "status") String propertyStatus,
            @Nullable @RequestParam(name = "category") String categoryName) {

        return propertyService.findPropertiesBySearchQuery(
                searchQuery, propertyStatus,
                categoryName, pageable);
    }

    @GetMapping("/{id}")
    public PropertyResponseDto getPropertyById(@PathVariable Long id) {

        return propertyResponseDtoMapper
                .map(propertyService.findById(id));
    }
}
