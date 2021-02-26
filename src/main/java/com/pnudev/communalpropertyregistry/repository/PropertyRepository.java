package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyStatisticsDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PropertyRepository extends CrudRepository<Property, Long> {

    @Query("SELECT " +
            "cbp.name as category, " +
            "count(p.property_status) as total_number, " +
            "count(case when p.property_status='RENT' then 1 end) as number_of_rented ," +
            "count(case when p.property_status='NON_RENT' then 1 end) as number_of_non_rented, " +
            "count(case when p.property_status='FIRST_OR_SECOND_TYPE_LIST' then 1 end) as number_of_first_or_second_type, " +
            "count(case when p.property_status='PRIVATIZED' then 1 end) as number_of_privatized, " +
            "count(case when p.property_status='USED_BY_CITY_COUNCIL' then 1 end) as  number_of_used_by_city_council " +
            "FROM category_by_purpose as cbp JOIN property as p " +
            "ON cbp.id = p.category_by_purpose_id " +
            "GROUP BY cbp.name ")
    List<PropertyStatisticsDto> getListOfStatistics();
}
