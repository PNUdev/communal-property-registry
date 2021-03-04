package com.pnudev.communalpropertyregistry.config;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class QueryDslConfiguration {

    @Bean
    public SQLQueryFactory queryFactory(DataSource dataSource) {
        return new SQLQueryFactory(new com.querydsl.sql.Configuration(new MySQLTemplates()), dataSource);
    }

}
