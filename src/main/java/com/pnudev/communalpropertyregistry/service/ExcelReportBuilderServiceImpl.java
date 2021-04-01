package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("excelReportBuilderServiceImpl")
public class ExcelReportBuilderServiceImpl extends AbstractExcelReportBuilderService
        implements ExcelReportBuilderService {

    public ExcelReportBuilderServiceImpl(PropertyService propertyService,
                                         AttachmentCategoryService attachmentCategoryService) {
        super(propertyService, attachmentCategoryService);
    }

    @Override
    protected List<PropertyResponseDto> getProperties(String searchQuery, String propertyStatus,
                                                      Long categoryByPurposeId, Pageable pageable) {
        return propertyService.findPropertiesBySearchQuery(searchQuery, propertyStatus,
                categoryByPurposeId, pageable).toList();
    }

    @Override
    protected String getFileName() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        return "properties_" + currentDateTime;
    }

}
