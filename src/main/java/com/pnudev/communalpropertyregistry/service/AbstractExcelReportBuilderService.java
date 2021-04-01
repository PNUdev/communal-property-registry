package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
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

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

@Slf4j
public abstract class AbstractExcelReportBuilderService {

    public static final String DEFAULT_CELL_VALUE = "-";

    protected final PropertyService propertyService;

    private final AttachmentCategoryService attachmentCategoryService;

    private final HSSFWorkbook workbook;

    private final HSSFSheet sheet;

    public AbstractExcelReportBuilderService(PropertyService propertyService,
                                             AttachmentCategoryService attachmentCategoryService) {
        this.workbook = new HSSFWorkbook();
        this.attachmentCategoryService = attachmentCategoryService;
        this.propertyService = propertyService;
        this.sheet = workbook.createSheet("Properties");
    }

    @SneakyThrows
    public void exportReport(String searchQuery, String propertyStatus, Long categoryByPurposeId,
                             HttpServletResponse response) {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=%s.xls", getFileName()));

        try (OutputStream outputStream = response.getOutputStream()) {

            Pageable wholePage = PageRequest.of(0, Integer.MAX_VALUE);
            List<PropertyResponseDto> properties = getProperties(searchQuery, propertyStatus,
                    categoryByPurposeId, wholePage);

            writeHeaderRow();
            writeDataRows(properties);

            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            log.error("Error while generating Excel report", e);
            throw new ServiceApiException("Помилка при генерації Excel звіту");
        }
    }

    abstract protected List<PropertyResponseDto> getProperties(String searchQuery, String propertyStatus,
                                                               Long categoryByPurposeId, Pageable pageable);

    abstract protected String getFileName();

    private void writeHeaderRow() {

        List<String> headers = new ArrayList<>(Arrays.asList("ID", "Посилання на зображення", "Адреса",
                "Довгота", "Широта", "Назва", "Назва категорії", "Статус", "Площа", "Площа для продажу",
                "Балансоутримувач", "Власник", "Дата завершення договору", "Сума(грн)"));

        headers.addAll(getAttachmentHeaders());

        Row row = sheet.createRow(0);
        CellStyle cellBoldStyle = getCellBoldStyle();

        IntStream.range(0, headers.size()).forEach(
                idx -> {
                    Cell cell = row.createCell(idx);
                    cell.setCellValue(headers.get(idx));
                    cell.setCellStyle(cellBoldStyle);

                    sheet.autoSizeColumn(idx);
                }
        );
    }

    private void writeDataRows(List<PropertyResponseDto> properties) {

        IntStream.range(0, properties.size()).forEach(
                idx -> {
                    Row row = sheet.createRow(idx + 1);
                    PropertyResponseDto property = properties.get(idx);

                    Field[] allFields = property.getClass().getDeclaredFields();

                    List<String> attachmentHeaders = getAttachmentHeaders();

                    int cellNumber = -1;

                    for (Field field : allFields) {
                        field.setAccessible(true);
                        Object object;

                        try {
                            object = field.get(property);
                        } catch (Exception e) {
                            log.error("Error while generating Excel report", e);
                            throw new ServiceApiException("Помилка при генерації Excel звіту");
                        }

                        if (isNull(object)) {
                            row.createCell(++cellNumber).setCellValue(DEFAULT_CELL_VALUE);

                        } else if (object instanceof Property.PropertyLocation) {
                            Property.PropertyLocation location = (Property.PropertyLocation) object;

                            row.createCell(++cellNumber).setCellValue(location.getLon());
                            row.createCell(++cellNumber).setCellValue(location.getLat());

                        } else if (object instanceof List<?>) {

                            Map<String, String> attachments = ((List<AttachmentResponseDto>) object).stream()
                                    .collect(Collectors.toMap(
                                            AttachmentResponseDto::getCategoryName,
                                            AttachmentResponseDto::getLink));

                            for (String header : attachmentHeaders) {
                                row.createCell(++cellNumber)
                                        .setCellValue(requireNonNullElse(attachments.get(header), DEFAULT_CELL_VALUE));
                            }

                        } else {
                            row.createCell(++cellNumber).setCellValue(object.toString());
                        }

                    }
                }
        );
    }

    private CellStyle getCellBoldStyle() {
        CellStyle cellBoldStyle = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setBold(true);
        cellBoldStyle.setFont(font);

        return cellBoldStyle;
    }

    protected List<String> getAttachmentHeaders() {

        return attachmentCategoryService.findAll().stream()
                .map(AttachmentCategory::getName)
                .sorted()
                .collect(Collectors.toList());

    }

}
