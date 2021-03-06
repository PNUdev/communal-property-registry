package com.pnudev.communalpropertyregistry.config;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class QueryDslConfig {

    @Bean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource) {
        return new SQLQueryFactory(
                new com.querydsl.sql.Configuration(new MySQLTemplates()),
                new TransactionAwareDataSourceProxy(dataSource));
    }

}