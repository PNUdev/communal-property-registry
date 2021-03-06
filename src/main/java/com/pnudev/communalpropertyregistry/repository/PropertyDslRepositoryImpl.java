package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.util.mapper.PropertyMapper;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;

@Repository
public class PropertyDslRepositoryImpl implements PropertyDslRepository {

    private final SQLQueryFactory queryFactory;
    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyDslRepositoryImpl(SQLQueryFactory queryFactory, PropertyMapper propertyMapper) {
        this.queryFactory = queryFactory;
        this.propertyMapper = propertyMapper;
    }

    @Override
    public PropertiesLocationsResponseDto findAllMapLocations(Predicate... where) {

         List<Tuple> properties = queryFactory
                .select(property.id, property.propertyStatus, property.lon, property.lat)
                .from(property)
                .where(where)
                .fetch();

        return new PropertiesLocationsResponseDto(properties.stream()
                .map(propertyMapper::mapToPropertyLocationDto).collect(Collectors.toList()));
    }

}
