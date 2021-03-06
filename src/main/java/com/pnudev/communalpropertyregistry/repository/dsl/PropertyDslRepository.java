package com.pnudev.communalpropertyregistry.repository.dsl;

import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.querydsl.core.types.Predicate;

public interface PropertyDslRepository {

    PropertiesLocationsResponseDto findAllMapLocations(Predicate... where);

}
