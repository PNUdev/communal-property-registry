package com.pnudev.communalpropertyregistry.repository.dsl;

import com.pnudev.communalpropertyregistry.domain.Property;
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
public class PropertyDslRepositoryImpl implements QueryDslRepository<Property> {

    private final SQLQueryFactory queryFactory;

    @Autowired
    public PropertyDslRepositoryImpl(SQLQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Property> findAll(Pageable pageable, Predicate... where) {

        List<Tuple> tuples = queryFactory
                .select(property.all())
                .from(property)
                .where(where)
                .orderBy(getOrderSpecifiers(pageable, Property.class))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        long total = queryFactory
                .query()
                .from(property)
                .where(where)
                .fetchCount();

        return new PageImpl<>(mapTupleToProperty(tuples), pageable, total);
    }

    @Override
    public List<Property> findAll(Predicate... where) {

        List<Tuple> properties = queryFactory
                .select(property.all())
                .from(property)
                .where(where)
                .fetch();

        return properties.stream()
                .map(this::mapTupleToProperty)
                .collect(Collectors.toList());
    }

    private List<Property> mapTupleToProperty(List<Tuple> properties) {
        return properties.stream().map(this::mapTupleToProperty).collect(Collectors.toList());
    }

    private Property mapTupleToProperty(Tuple tuple) {

        Property.PropertyLocation propertyLocation = Property.PropertyLocation.builder()
                .lat(tuple.get(property.lat))
                .lon(tuple.get(property.lon))
                .build();

        return Property.builder()
                .id(tuple.get(property.id))
                .imageUrl(tuple.get(property.imageUrl))
                .address(tuple.get(property.address))
                .propertyLocation(propertyLocation)
                .name(tuple.get(property.name))
                .categoryByPurposeId(tuple.get(property.categoryByPurposeId))
                .propertyStatus(Property.PropertyStatus.valueOf(tuple.get(property.propertyStatus)))
                .area(tuple.get(property.area))
                .areaTransferred(tuple.get(property.areaTransferred))
                .balanceHolder(tuple.get(property.balanceHolder))
                .owner(tuple.get(property.owner))
                .leaseAgreementEndDate(tuple.get(property.leaseAgreementEndDate).toLocalDate())
                .amountOfRent(tuple.get(property.amountOfRent))
                .isAreaTransferredPubliclyViewable(tuple.get(property.isAreaTransferredPubliclyViewable))
                .isBalanceHolderPubliclyViewable(tuple.get(property.isBalanceHolderPubliclyViewable))
                .isOwnerPubliclyViewable(tuple.get(property.isOwnerPubliclyViewable))
                .isLeaseAgreementEndDatePubliclyViewable(tuple.get(property.isLeaseAgreementEndDatePubliclyViewable))
                .isAmountOfRentPubliclyViewable(tuple.get(property.isAmountOfRentPubliclyViewable))
                .build();
    }

}