package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.PropertyStatisticResponseDto;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final PropertyRepository propertyRepository;

    @Autowired
    public StatisticServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public PropertyStatisticResponseDto getStatistic() {
        return new PropertyStatisticResponseDto(propertyRepository.getListOfStatistic());
    }

}
