package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyStatisticDto;
import com.pnudev.communalpropertyregistry.dto.PropertyStatisticResponseDto;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        List<Property> properties = propertyRepository.findAll();
        List<CategoryByPurpose> categories = categoryByPurposeRepository.findAll();

        List<PropertyStatisticDto> propertyStatistic = new ArrayList<>();

        for(CategoryByPurpose temp : categories){

            PropertyStatisticDto propertyStatisticDto = PropertyStatisticDto.builder()
                    .category(temp.getName())
                    .totalNumber((int) properties.stream()
                            .filter(property -> property.getCategoryByPurposeId().equals(temp.getId()))
                            .count())
                    .numberOfNonRented(getNumberOfRepetitions(properties, temp.getId(),
                            Property.PropertyStatus.NON_RENT))
                    .numberOfRented(getNumberOfRepetitions(properties, temp.getId(),
                            Property.PropertyStatus.RENT))
                    .numberOfPrivatized(getNumberOfRepetitions(properties, temp.getId(),
                            Property.PropertyStatus.PRIVATIZED))
                    .numberOfListed(getNumberOfRepetitions(properties, temp.getId(),
                            Property.PropertyStatus.FIRST_OR_SECOND_TYPE_LIST))
                    .numberOfUsedByCityCouncil(getNumberOfRepetitions(properties, temp.getId(),
                            Property.PropertyStatus.USED_BY_CITY_COUNCIL))
                    .build();

            propertyStatistic.add(propertyStatisticDto);
        }

        return new PropertyStatisticResponseDto(propertyStatistic);
    }

    public int getNumberOfRepetitions(List<Property> properties, Long categoryId,
                                      Property.PropertyStatus propertyStatus){
        return (int) properties.stream()
                .filter(property -> property.getCategoryByPurposeId().equals(categoryId)
                        && property.getPropertyStatus().equals(propertyStatus))
                .count();
    }

}
