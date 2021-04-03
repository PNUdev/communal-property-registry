package com.pnudev.communalpropertyregistry.controller.api;

import com.pnudev.communalpropertyregistry.dto.PropertiesLocationsResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.service.ExcelReportBuilderService;
import com.pnudev.communalpropertyregistry.service.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    private final ExcelReportBuilderService publicExcelReportBuilderService;

    private final ExcelReportBuilderService fullExcelReportBuilderService;

    @Autowired
    public PropertyController(PropertyService locationService,
                              @Qualifier("excelReportBuilderServiceImpl") ExcelReportBuilderService publicExcelReportBuilderService,
                              @Qualifier("fullExcelReportBuilderServiceImpl") ExcelReportBuilderService fullExcelReportBuilderService) {

        this.propertyService = locationService;
        this.publicExcelReportBuilderService = publicExcelReportBuilderService;
        this.fullExcelReportBuilderService = fullExcelReportBuilderService;
    }

    @GetMapping
    public Page<PropertyResponseDto> getPropertiesBySearch(
            @PageableDefault Pageable pageable,
            @Nullable @RequestParam(name = "q") String searchQuery,
            @Nullable @RequestParam(name = "status") String propertyStatus,
            @Nullable @RequestParam(name = "category") Long categoryByPurposeId) {

        return propertyService.findPropertiesBySearchQuery(
                searchQuery, propertyStatus, categoryByPurposeId, pageable);
    }

    @GetMapping("/{id}")
    public PropertyResponseDto getPropertyById(@PathVariable Long id) {
        return propertyService.findById(id);
    }

    @GetMapping("/map-locations")
    public PropertiesLocationsResponseDto getMapLocations(@Nullable @RequestParam(name = "q") String searchQuery,
                                                          @Nullable @RequestParam(name = "status") String propertyStatus,
                                                          @Nullable @RequestParam(name = "category") Long categoryByPurposeId) {

        return propertyService.getMapLocations(searchQuery, propertyStatus, categoryByPurposeId);
    }

    @GetMapping("/report")
    public void getPropertiesExcelReport(HttpServletResponse response,
                                         @Nullable @RequestParam(name = "q") String searchQuery,
                                         @Nullable @RequestParam(name = "status") String propertyStatus,
                                         @Nullable @RequestParam(name = "category") Long categoryByPurposeId) {

        log.info("Public report request");
        publicExcelReportBuilderService.exportReport(searchQuery, propertyStatus, categoryByPurposeId, response);
        log.info("Public report successfully generated");
    }


    @GetMapping("/admin/report")
    public void getFullPropertiesExcelReport(HttpServletResponse response,
                                             @Nullable @RequestParam(name = "q") String searchQuery,
                                             @Nullable @RequestParam(name = "status") String propertyStatus,
                                             @Nullable @RequestParam(name = "category") Long categoryByPurposeId) {

        log.info("Full report request");
        fullExcelReportBuilderService.exportReport(searchQuery, propertyStatus, categoryByPurposeId, response);
        log.info("Full report successfully generated");
    }
}

