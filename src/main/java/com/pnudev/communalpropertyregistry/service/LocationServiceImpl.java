package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationDto;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationResponseDto;
import com.pnudev.communalpropertyregistry.repository.PropertyLocationDslRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;
import static java.util.Objects.nonNull;

@Service
public class LocationServiceImpl implements LocationService{

    private final PropertyLocationDslRepository propertyLocationDslRepository;

    @Autowired
    public LocationServiceImpl(PropertyLocationDslRepository propertyLocationDslRepository) {
        this.propertyLocationDslRepository = propertyLocationDslRepository;
    }

    @Override
    public PropertyLocationResponseDto getLocation(String searchQuery, String propertyStatus, Long categoryByPurposeId) {

        List<Predicate> predicates = new ArrayList<>();

        if (nonNull(searchQuery)) {
            predicates.add(property.name.contains(searchQuery).or(property.address.contains(searchQuery)));
        }

        if (nonNull(categoryByPurposeId)) {
            predicates.add(property.categoryByPurposeId.eq(categoryByPurposeId));
        }

        if (nonNull(propertyStatus)) {
            predicates.add(property.propertyStatus
                    .contains(String.valueOf(Property.PropertyStatus.valueOf(propertyStatus.toUpperCase()))));
        }

        List<Tuple> properties = propertyLocationDslRepository.findAll(predicates.toArray(Predicate[]::new));

        return new PropertyLocationResponseDto(properties.stream()
                .map(this::mapPropertyToPropertyLocationDto).collect(Collectors.toList()));
    }


    private PropertyLocationDto mapPropertyToPropertyLocationDto(Tuple tuple) {

        return PropertyLocationDto.builder()
                .propertyId(tuple.get(property.id))
                .propertyStatus(Property.PropertyStatus.valueOf(tuple.get(property.propertyStatus)))
                .lat(tuple.get(property.lat))
                .lon(tuple.get(property.lon))
                .build();
    }

}
