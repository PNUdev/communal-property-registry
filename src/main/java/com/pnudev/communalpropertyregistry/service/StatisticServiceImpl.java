package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.PropertyStatisticResponseDto;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatisticServiceImpl implements StatisticService {

    private final PropertyRepository propertyRepository;

    @Autowired
    public StatisticServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public PropertyStatisticResponseDto getStatistics() {

        log.debug("Property statistics collection has started!");

        return new PropertyStatisticResponseDto(propertyRepository.getListOfStatistics());
    }

}
