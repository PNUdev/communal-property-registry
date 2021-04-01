package com.pnudev.communalpropertyregistry.service;

import javax.servlet.http.HttpServletResponse;

public interface ExcelReportBuilderService {

    void exportReport(String searchQuery, String propertyStatus,
                      Long categoryByPurposeId, HttpServletResponse response);

}
