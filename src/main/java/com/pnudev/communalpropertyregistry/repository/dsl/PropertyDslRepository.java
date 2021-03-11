package com.pnudev.communalpropertyregistry.repository.dsl;

import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyDslRepository {

    PropertiesLocationsResponseDto findAllMapLocations(Predicate... where);

    Page<PropertyResponseDto> findAll(Pageable pageable, Predicate... where);

}
