package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyStatisticResponseDto;

import java.util.List;

public interface StatisticService {

    PropertyStatisticResponseDto getStatistic();

    int getNumberOfRepetitions(List<Property> properties, Long categoryId,
                               Property.PropertyStatus propertyStatus);

}
