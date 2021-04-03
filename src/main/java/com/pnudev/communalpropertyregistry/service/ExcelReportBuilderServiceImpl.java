package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("excelReportBuilderServiceImpl")
public class ExcelReportBuilderServiceImpl extends AbstractExcelReportBuilderService {

    private final PropertyService propertyService;

    @Autowired
    public ExcelReportBuilderServiceImpl(AttachmentCategoryService attachmentCategoryService,
                                         PropertyService propertyService) {
        super(attachmentCategoryService);
        this.propertyService = propertyService;
    }

    @Override
    protected List<PropertyResponseDto> getProperties(String searchQuery, String propertyStatus,
                                                      Long categoryByPurposeId) {
        return propertyService.findPropertiesBySearchQuery(searchQuery, propertyStatus,
                categoryByPurposeId, PageRequest.of(0, Integer.MAX_VALUE)).toList();
    }

    @Override
    protected String getFileName() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        return "properties_" + currentDateTime;
    }

}
