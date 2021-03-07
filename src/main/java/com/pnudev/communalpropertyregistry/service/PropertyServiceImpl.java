package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.exception.ServiceException;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import com.pnudev.communalpropertyregistry.repository.dsl.PropertyDslRepository;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyDslRepository propertyLocationDslRepository;

    private final PropertyRepository propertyRepository;

    // TODO: 21.02.21 Replace repository with service, when second will be implemented
    private final CategoryByPurposeRepository categoryByPurposeRepository;

    @Autowired
    public PropertyServiceImpl(PropertyDslRepository propertyLocationDslRepository,
                               PropertyRepository propertyRepository,
                               CategoryByPurposeRepository categoryByPurposeRepository) {

        this.propertyLocationDslRepository = propertyLocationDslRepository;
        this.propertyRepository = propertyRepository;
        this.categoryByPurposeRepository = categoryByPurposeRepository;
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
    public Property findById(Long id){
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Приміщення не знайдено"));
    }

    @Override
    public void deleteById(Long id) {
        propertyRepository.deleteById(id);
    }

}
