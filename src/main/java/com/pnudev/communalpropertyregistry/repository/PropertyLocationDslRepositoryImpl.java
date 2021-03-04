package com.pnudev.communalpropertyregistry.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.mysql.MySQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;

@Repository
public class PropertyLocationDslRepositoryImpl implements PropertyLocationDslRepository {

    private final DataSource dataSource;

    @Autowired
    public PropertyLocationDslRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Tuple> findAll(Predicate... where) {
        try {
            List<Tuple> properties = new MySQLQuery<>(dataSource.getConnection())
                    .select(property.all())
                    .from(property)
                    .where(where)
                    .fetch();

            return properties;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Catch SQLException");
        }
    }

}
