package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationDto;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationResponseDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;

@Repository
public class PropertyLocationDslRepositoryImpl implements PropertyLocationDslRepository {

    private final SQLQueryFactory queryFactory;

    @Autowired
    public PropertyLocationDslRepositoryImpl(SQLQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public PropertyLocationResponseDto findAll(Predicate... where) {

         List<Tuple> properties = queryFactory
                .select(property.id, property.propertyStatus, property.lon, property.lat)
                .from(property)
                .where(where)
                .fetch();

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
