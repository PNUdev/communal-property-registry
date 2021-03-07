package com.pnudev.communalpropertyregistry.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryDslRepository<T> {

    Page<T> findAll(Pageable pageable, Predicate... where);

    default OrderSpecifier[] getOrderSpecifiers(Pageable pageable, Class<?> aClass) {

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
