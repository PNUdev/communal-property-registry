package com.pnudev.communalpropertyregistry.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.stereotype.Repository;
import java.util.List;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;

@Repository
public class PropertyLocationDslRepositoryImpl implements PropertyLocationDslRepository {

    private final SQLQueryFactory queryFactory;

    public PropertyLocationDslRepositoryImpl(SQLQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Tuple> findAll(Predicate... where) {

        return queryFactory
                .select(property.all())
                .from(property)
                .where(where)
                .fetch();

    }

}
