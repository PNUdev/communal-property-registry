package com.pnudev.communalpropertyregistry.service;

import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Pageable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ExcelReportBuilderServiceImpl implements ExcelReportBuilderService {

    private final PropertyService propertyService;

    private final HSSFWorkbook workbook;

    private final HSSFSheet sheet;


    public ExcelReportBuilderServiceImpl(PropertyService propertyService) {
        this.workbook = new HSSFWorkbook();
        this.propertyService = propertyService;
        this.sheet = workbook.createSheet("Properties");
    }

    @Override
    @SneakyThrows
    public void exportReport(String searchQuery, String propertyStatus, Long categoryByPurposeId,
                             HttpServletResponse response) {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=%s.xls", getFileName()));

        writeHeaderRow();
        writeDataRows(searchQuery, propertyStatus, categoryByPurposeId);

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    // Write header rows with font-style: bold
    private void writeHeaderRow() {

        String[] columnNames = {"ID", "Назва", "Посилання на зображення", "Адреса", "Довгота", "Широта",
                "Назва категорії", "Площа", "Площа для продажу", "Балансоутримувач", "Власник",
                "Дата завершення договору", "Сума(грн)", "Прикріплення"};

        Row row = sheet.createRow(0);
        CellStyle cellBoldStyle = getCellBoldStyle();

        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cellBoldStyle);

            sheet.autoSizeColumn(i);
        }

    }

    private void writeDataRows(String searchQuery, String propertyStatus, Long categoryByPurposeId) {

//        Page<PropertyResponseDto> properties = propertyService.findPropertiesBySearchQuery(
//               searchQuery,  propertyStatus,  categoryByPurposeId, Pageable.unpaged());

    }

    private String getFileName(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        return "properties_" + currentDateTime;
    }

    private CellStyle getCellBoldStyle() {
        CellStyle cellBoldStyle = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        cellBoldStyle.setFont(font);

        return cellBoldStyle;
    }

}
