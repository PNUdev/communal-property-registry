package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.exception.ServiceApiException;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import com.pnudev.communalpropertyregistry.repository.dsl.PropertyDslRepository;
import com.pnudev.communalpropertyregistry.util.mapper.PropertyMapper;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;
import static java.util.Objects.nonNull;


@Slf4j
@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    private final CategoryByPurposeService categoryByPurposeService;

    private final PropertyDslRepository propertyDslRepository;

    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyServiceImpl(CategoryByPurposeService categoryByPurposeService,
                               PropertyDslRepository propertyDslRepository,
                               PropertyRepository propertyRepository,
                               PropertyMapper propertyMapper) {

        this.categoryByPurposeService = categoryByPurposeService;
        this.propertyDslRepository = propertyDslRepository;
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    @Override
    public PropertiesLocationsResponseDto getMapLocations(String searchQuery, String propertyStatus, Long categoryByPurposeId) {

        log.info("Get property location request");

        List<Predicate> predicates = new ArrayList<>();

        if (nonNull(searchQuery)) {
            predicates.add(property.name.contains(searchQuery).or(property.address.contains(searchQuery)));
        }

        if (nonNull(categoryByPurposeId)) {
            predicates.add(property.categoryByPurposeId.eq(categoryByPurposeId));
        }

        if (nonNull(propertyStatus)) {
            predicates.add(property.propertyStatus
                    .eq(String.valueOf(Property.PropertyStatus.valueOf(propertyStatus.toUpperCase()))));
        }

        return propertyDslRepository
                .findAllMapLocations(predicates.toArray(Predicate[]::new));
    }

    @Override
    public Page<PropertyResponseDto> findPropertiesBySearchQuery(String searchQuery, String propertyStatus,
                                                         Long categoryByPurposeId, Pageable pageable) {

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(property.id.isNotNull());

        if (nonNull(searchQuery)) {

            predicates.add(property.name.contains(searchQuery)
                    .or(property.address.contains(searchQuery)));
        }

        if (nonNull(propertyStatus)) {

            Property.PropertyStatus status = Property.PropertyStatus.fromName(propertyStatus)
                    .orElseThrow(() -> new ServiceApiException("Вказана категорія не існує!"));

            predicates.add(property.propertyStatus.eq(status.name()));
        }

        if (nonNull(categoryByPurposeId)) {

            CategoryByPurpose category = categoryByPurposeService
                    .findById(categoryByPurposeId);

            predicates.add(property.categoryByPurposeId.eq(category.getId()));
        }

        // I decided to implement it this way in order to avoid creating additional method in repository

        Page<Property> propertyPages = propertyDslRepository
                .findAll(pageable, predicates.toArray(Predicate[]::new));

        List<PropertyResponseDto> content = propertyMapper
                .mapToPropertyResponseDto(propertyPages.getContent());

        return new PageImpl<>(content, pageable, propertyPages.getTotalElements());
    }

    @Override
    public PropertyResponseDto findById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ServiceApiException("Приміщення не знайдено!"));

        return propertyMapper.mapToPropertyResponseDto(property);
    }

}
