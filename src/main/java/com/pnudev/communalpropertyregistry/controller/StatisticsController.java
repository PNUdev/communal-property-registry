package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.dto.PropertyStatisticsResponseDto;
import com.pnudev.communalpropertyregistry.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private StatisticsService statisticService;

    @Autowired
    public StatisticsController(StatisticsService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    public PropertyStatisticsResponseDto getStatistics(){
        return statisticService.getStatistics();
    }

}
