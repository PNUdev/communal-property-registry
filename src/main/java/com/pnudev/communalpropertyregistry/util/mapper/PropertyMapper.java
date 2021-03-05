package com.pnudev.communalpropertyregistry.util.mapper;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationDto;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Component;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;

@Component
public class PropertyMapper {

    public PropertyLocationDto mapToPropertyLocationDto(Tuple tuple) {

        return PropertyLocationDto.builder()
                .propertyId(tuple.get(property.id))
                .propertyStatus(Property.PropertyStatus.valueOf(tuple.get(property.propertyStatus)))
                .lat(tuple.get(property.lat))
                .lon(tuple.get(property.lon))
                .build();
    }

}
