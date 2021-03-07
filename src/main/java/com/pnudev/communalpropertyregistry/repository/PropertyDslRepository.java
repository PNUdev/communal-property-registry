package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyDslRepository {

    Page<Property> findAll(Pageable pageable, Predicate... where);

}
