package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.sql.mysql.MySQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;

@Repository
public class PropertyDslRepositoryImpl implements PropertyDslRepository {

    private final DataSource dataSource;

    public PropertyDslRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Page<Property> findAll(Pageable pageable, Predicate... where) {
        try {
            List<Tuple> tuples = new MySQLQuery<>(dataSource.getConnection())
                    .select(property.all())
                    .from(property)
                    .where(where)
                    .orderBy(getOrderSpecifiers(pageable, Property.class))
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetch();

            long total = new MySQLQuery<>(dataSource.getConnection())
                    .from(property)
                    .where(where)
                    .fetchCount();

            return new PageImpl<>(mapTupleOfPropertiesToList(tuples), pageable, total);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Catch SQLException");
        }
    }

    private List<Property> mapTupleOfPropertiesToList(List<Tuple> properties) {
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

    private OrderSpecifier[] getOrderSpecifiers(Pageable pageable, Class<?> aClass) {

        String className = aClass.getSimpleName();

        final String orderVariable = String.valueOf(Character.toLowerCase(className.charAt(0))).concat(className.substring(1));

        return pageable.getSort().stream()
                .map(order -> new OrderSpecifier(
                        Order.valueOf(order.getDirection().toString()),
                        new PathBuilder(aClass, orderVariable).get(order.getProperty()))
                )
                .toArray(OrderSpecifier[]::new);
    }

}
