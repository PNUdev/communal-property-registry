package com.pnudev.communalpropertyregistry.controller.admin;

import com.pnudev.communalpropertyregistry.service.DetailStatisticsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DetailStatisticsAdminController {

    private final DetailStatisticsAdminService detailStatisticsAdminService;

    @Autowired
    public DetailStatisticsAdminController(DetailStatisticsAdminService detailStatisticsAdminService) {
        this.detailStatisticsAdminService = detailStatisticsAdminService;
    }

    @RequestMapping("/detail-statistics")
    public String detailStatistics(Model model) {

        String detailStatistics = detailStatisticsAdminService.getDetailStatistics();

        model.addAttribute("detailStatistics", detailStatistics);

        return "admin/detail-statistics/show";
    }

}
