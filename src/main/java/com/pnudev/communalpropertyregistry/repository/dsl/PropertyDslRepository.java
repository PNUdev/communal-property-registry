package com.pnudev.communalpropertyregistry.repository.dsl;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertyDslRepository {

    PropertiesLocationsResponseDto findAllMapLocations(Predicate... where);

    Page<Property> findAll(Pageable pageable, Predicate... where);

}
