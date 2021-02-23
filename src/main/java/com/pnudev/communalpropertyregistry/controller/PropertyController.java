package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.dto.PropertyStatisticResponseDto;
import com.pnudev.communalpropertyregistry.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PropertyController {

    public StatisticService statisticService;

    @Autowired
    public PropertyController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/statistics")
    public PropertyStatisticResponseDto getStatistic(){
        return statisticService.getStatistic();
    }

}
