package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.response.AttachmentResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.exception.ServiceApiException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
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

        try (OutputStream outputStream = response.getOutputStream()) {
            Pageable wholePage = PageRequest.of(0, Integer.MAX_VALUE);
            List<PropertyResponseDto> properties = propertyService.findPropertiesBySearchQuery(
                    searchQuery, propertyStatus, categoryByPurposeId, wholePage).toList();

            writeHeaderRow();
            writeDataRows(properties);

            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            log.error("Error while generating Excel report", e);
            throw new ServiceApiException("Помилка при генерації Excel звіту");
        }
    }

    // Write header rows with font-style: bold
    private void writeHeaderRow() {

        String[] columnNames = {"ID", "Посилання на зображення", "Адреса", "Довгота", "Широта", "Назва",
                "Назва категорії", "Статус", "Площа", "Площа для продажу", "Балансоутримувач", "Власник",
                "Дата завершення договору", "Сума(грн)", "Посилання на прикріплення"};

        Row row = sheet.createRow(0);
        CellStyle cellBoldStyle = getCellBoldStyle();

        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cellBoldStyle);

            sheet.autoSizeColumn(i);
        }

    }

    private void writeDataRows(List<PropertyResponseDto> properties) {

        IntStream.range(0, properties.size()).forEach(
                idx -> {
                    Row row = sheet.createRow(idx + 1);
                    PropertyResponseDto property = properties.get(idx);

                    System.out.println(property.toString());

                    Field[] allFields = property.getClass().getDeclaredFields();

                    //Write cells of property
                    int cellCount = 0;

                    for (Field field : allFields) {
                        field.setAccessible(true);
                        Object value;

                        try {
                            value = field.get(property);
                        } catch (Exception e) {
                            log.error("Error while generating Excel report", e);
                            throw new ServiceApiException("Помилка при генерації Excel звіту");
                        }

                        Cell cell = row.createCell(cellCount);

                        if (value == null) {
                            cell.setCellValue("-");
                        } else if (value instanceof Property.PropertyLocation) {
                            Property.PropertyLocation location = (Property.PropertyLocation) value;

                            cell.setCellValue(location.getLon());

                            row.createCell(++cellCount)
                                    .setCellValue(location.getLat());
                        } else if (value instanceof List<?>) {

                            if (((List<?>) value).isEmpty()) {
                                cell.setCellValue("-");
                            } else {
                                for (Object element : (List<?>) value) {
                                    if (element instanceof AttachmentResponseDto) {
                                        String link = ((AttachmentResponseDto) element).getLink();

                                        row.createCell(cellCount++).setCellValue(link);
                                    }
                                }
                            }

                        } else {
                            cell.setCellValue(value.toString());
                        }

                        cellCount++;
                    }

                }
        );
    }

    private String getFileName() {
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
