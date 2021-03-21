package com.pnudev.communalpropertyregistry.repository.dsl;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyDslRepository {

    Page<Property> findAll(Pageable pageable, Predicate... where);

    PropertiesLocationsResponseDto findAllMapLocations(Predicate... where);

}
