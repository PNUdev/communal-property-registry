package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyStatisticDto;
import com.pnudev.communalpropertyregistry.dto.PropertyStatisticResponseDto;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService{

    private final PropertyRepository propertyRepository;

    private final CategoryByPurposeRepository categoryByPurposeRepository;

    @Autowired
    public StatisticServiceImpl(PropertyRepository propertyRepository, CategoryByPurposeRepository categoryByPurposeRepository) {
        this.propertyRepository = propertyRepository;
        this.categoryByPurposeRepository = categoryByPurposeRepository;
    }

    @Override
    public PropertyStatisticResponseDto getStatistic() {

        List<Property> properties = (List<Property>) propertyRepository.findAll();

        List<CategoryByPurpose> categories = (List<CategoryByPurpose>) categoryByPurposeRepository.findAll();

        List<PropertyStatisticDto> propertyStatistic = categories.stream()
                .map((category) ->
                        PropertyStatisticDto.builder()
                                .category(category.getName())

                                .totalNumber(properties.stream()
                                        .filter(property -> property.getCategoryByPurposeId().equals(category.getId()))
                                        .count()
                                )
                                .numberOfNonRented(
                                        propertyRepository.countPropertiesByCategoryByPurposeIdEqualsAndPropertyStatusEquals(
                                                category.getId(),
                                                Property.PropertyStatus.NON_RENT)
                                )
                                .numberOfRented(
                                        propertyRepository.countPropertiesByCategoryByPurposeIdEqualsAndPropertyStatusEquals(
                                                category.getId(),
                                                Property.PropertyStatus.RENT)
                                )
                                .numberOfPrivatized(
                                        propertyRepository.countPropertiesByCategoryByPurposeIdEqualsAndPropertyStatusEquals(
                                                category.getId(),
                                                Property.PropertyStatus.PRIVATIZED)
                                )
                                .numberOfListed(
                                        propertyRepository.countPropertiesByCategoryByPurposeIdEqualsAndPropertyStatusEquals(
                                                category.getId(),
                                                Property.PropertyStatus.FIRST_OR_SECOND_TYPE_LIST)
                                )
                                .numberOfUsedByCityCouncil(
                                        propertyRepository.countPropertiesByCategoryByPurposeIdEqualsAndPropertyStatusEquals(
                                                category.getId(),
                                                Property.PropertyStatus.USED_BY_CITY_COUNCIL)
                                )
                                .build()
                ).collect(Collectors.toList());

        return new PropertyStatisticResponseDto(propertyStatistic);
    }

}
