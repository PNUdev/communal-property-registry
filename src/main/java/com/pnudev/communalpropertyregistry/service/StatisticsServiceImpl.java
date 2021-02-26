package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.PropertyStatisticsResponseDto;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final PropertyRepository propertyRepository;

    @Autowired
    public StatisticsServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public PropertyStatisticsResponseDto getStatistics() {

        log.info("Get property statistics request");

        return new PropertyStatisticsResponseDto(propertyRepository.getListOfStatistics());
    }

}
