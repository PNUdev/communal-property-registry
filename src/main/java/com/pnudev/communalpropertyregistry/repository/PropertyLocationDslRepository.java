package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.dto.PropertyLocationResponseDto;
import com.querydsl.core.types.Predicate;

public interface PropertyLocationDslRepository {

    PropertyLocationResponseDto findAll(Predicate... where);

}
