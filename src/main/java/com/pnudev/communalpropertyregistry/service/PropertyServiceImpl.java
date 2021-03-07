package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.exception.ServiceException;
import com.pnudev.communalpropertyregistry.repository.PropertyDslRepository;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import com.pnudev.communalpropertyregistry.repository.dsl.PropertyLocationDslRepository;
import com.pnudev.communalpropertyregistry.util.mapper.PropertyToPropertyResponseDtoMapper;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyLocationDslRepository propertyLocationDslRepository;

    private final PropertyRepository propertyRepository;

    private final CategoryByPurposeService categoryByPurposeService;

    private final PropertyDslRepository propertyDslRepository;

    private final PropertyToPropertyResponseDtoMapper propertyResponseDtoMapper;


    @Autowired
    public PropertyServiceImpl(PropertyLocationDslRepository propertyLocationDslRepository,
                               PropertyRepository propertyRepository,
                               CategoryByPurposeService categoryByPurposeService,
                               PropertyDslRepository propertyDslRepository,
                               PropertyToPropertyResponseDtoMapper propertyResponseDtoMapper) {

        this.propertyLocationDslRepository = propertyLocationDslRepository;
        this.propertyRepository = propertyRepository;
        this.categoryByPurposeService = categoryByPurposeService;
        this.propertyDslRepository = propertyDslRepository;
        this.propertyResponseDtoMapper = propertyResponseDtoMapper;
    }

    @Override
    public PropertiesLocationsResponseDto getMapLocations(String searchQuery,
                                                          String propertyStatus,
                                                          Long categoryByPurposeId) {

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

        return propertyLocationDslRepository.findAllMapLocations(predicates.toArray(Predicate[]::new));

    }

    @Override
    public Page<PropertyResponseDto> findPropertiesBySearchQuery(String searchQuery, String propertyStatus,
                                                         Long categoryByPurposeId, Pageable pageable) {

        // Used for predicate initialization.
        // Following query predicate will return all records

        BooleanExpression predicate = property.id.isNotNull();
        if (nonNull(searchQuery)) {

            predicate = predicate.andAnyOf(
                    property.name.contains(searchQuery),
                    property.address.contains(searchQuery));
        }

        if (nonNull(propertyStatus)) {

            Property.PropertyStatus status = Optional.of(
                    Property.PropertyStatus.valueOf(propertyStatus))
                        .orElseThrow(() -> new ServiceException("Вказаної категорії не існує"));

            predicate = predicate.and(property.propertyStatus.eq(status.name()));
        }

        if (nonNull(categoryByPurposeId)) {

            CategoryByPurpose category = categoryByPurposeService
                    .findById(categoryByPurposeId);

            predicate = predicate.and(property.categoryByPurposeId.eq(category.getId()));
        }

        Page<Property> properties = propertyDslRepository.findAll(pageable, predicate);

        return properties.map(propertyResponseDtoMapper::map);
    }

    @Override
    public Property findById(Long id){
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Приміщення не знайдено"));
    }

    @Override
    public void deleteById(Long id) {
        propertyRepository.deleteById(id);
    }

}
