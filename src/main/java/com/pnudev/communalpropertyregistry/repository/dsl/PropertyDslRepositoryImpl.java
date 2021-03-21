package com.pnudev.communalpropertyregistry.repository.dsl;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.util.mapper.PropertyMapper;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Property> findAll(Pageable pageable, Predicate... where) {

        List<Tuple> tuples = queryFactory
                .select(property.all())
                .from(property)
                .where(where)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl<>(propertyMapper.mapToProperties(tuples), pageable, countAll(where));
    }

    private long countAll(Predicate... where) {
        return queryFactory
                .query()
                .from(property)
                .where(where)
                .fetchCount();
    }

}