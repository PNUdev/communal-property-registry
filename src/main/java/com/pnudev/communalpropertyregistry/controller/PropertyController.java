package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.exception.ServiceException;
import com.pnudev.communalpropertyregistry.service.PropertyService;
import com.pnudev.communalpropertyregistry.util.mapper.PropertyToPropertyResponseDtoMapper;
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

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/property")
public class PropertyController {

    @Value("${rest.pagination.size}")
    private Integer pageSize;

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
            @Nullable @RequestParam(name = "page") Integer page,
            @Nullable @RequestParam(name = "q") String searchQuery,
            @Nullable @RequestParam(name = "status") String propertyStatus,
            @Nullable @RequestParam(name = "category") Long categoryByPurposeId) {

        page = Optional.ofNullable(page).orElse(0);

        if (page < 0) {
            throw new ServiceException("Page index must not be less than zero");
        }

        return propertyService.findPropertiesBySearchQuery(
                searchQuery, propertyStatus,
                categoryByPurposeId,
                PageRequest.of(page, pageSize));
    }

    @GetMapping("/{id}")
    public PropertyResponseDto getPropertyById(@PathVariable Long id) {

        return propertyResponseDtoMapper
                .map(propertyService.findById(id));
    }

}
