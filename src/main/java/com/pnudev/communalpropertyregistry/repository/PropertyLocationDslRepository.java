package com.pnudev.communalpropertyregistry.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;

import java.util.List;

public interface PropertyLocationDslRepository {

    List<Tuple> findAll(Predicate... where);

}
