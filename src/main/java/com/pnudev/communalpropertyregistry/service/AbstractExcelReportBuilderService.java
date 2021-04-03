package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.dto.response.AttachmentResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.exception.ServiceApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.nonNull;

@Slf4j
public abstract class AbstractExcelReportBuilderService implements ExcelReportBuilderService {

    private static final List<String> BASE_HEADERS = Arrays.asList("ID", "Посилання на зображення", "Адреса",
            "Довгота", "Широта", "Назва", "Назва категорії", "Статус", "Площа", "Площа для продажу",
            "Балансоутримувач", "Власник", "Дата завершення договору", "Сума(грн)");

    public static final String DEFAULT_CELL_VALUE = "-";

    private final AttachmentCategoryService attachmentCategoryService;

    public AbstractExcelReportBuilderService(AttachmentCategoryService attachmentCategoryService) {
        this.attachmentCategoryService = attachmentCategoryService;
    }

    public void exportReport(String searchQuery, String propertyStatus, Long categoryByPurposeId,
                             HttpServletResponse response) {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=%s.xls", getFileName()));

        try (OutputStream outputStream = response.getOutputStream()) {

            List<PropertyResponseDto> properties = getProperties(searchQuery, propertyStatus,
                    categoryByPurposeId);

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Properties");

            writeHeaderRow(workbook, sheet);
            writeDataRows(properties, sheet);

            workbook.write(outputStream);
            outputStream.close();
            workbook.close();

        } catch (Exception e) {
            log.error("Error while generating Excel report", e);
            throw new ServiceApiException("Помилка при генерації Excel звіту");
        }
    }

    abstract protected List<PropertyResponseDto> getProperties(String searchQuery, String propertyStatus,
                                                               Long categoryByPurposeId);

    abstract protected String getFileName();

    private void writeHeaderRow(HSSFWorkbook workbook, HSSFSheet sheet) {

        ArrayList<String> headers = new ArrayList<>(BASE_HEADERS);
        headers.addAll(getAttachmentHeaders());

        Row row = sheet.createRow(0);
        CellStyle cellBoldStyle = getCellBoldStyle(workbook);

        IntStream.range(0, headers.size()).forEach(
                idx -> {
                    Cell cell = row.createCell(idx);
                    cell.setCellValue(headers.get(idx));
                    cell.setCellStyle(cellBoldStyle);

                    sheet.autoSizeColumn(idx);
                }
        );
    }

    private void writeDataRows(List<PropertyResponseDto> properties, HSSFSheet sheet) {

        IntStream.range(0, properties.size()).forEach(
                idx -> {
                    Row row = sheet.createRow(idx + 1);
                    PropertyResponseDto property = properties.get(idx);

                    List<String> attachmentHeaders = getAttachmentHeaders();

                    int cellNumber = -1;

                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getId()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getImageUrl()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getAddress()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getPropertyLocation().getLon()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getPropertyLocation().getLat()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getName()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getCategoryByPurposeName()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getPropertyStatus()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getArea()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getAreaTransferred()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getBalanceHolder()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getOwner()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getLeaseAgreementEndDate()));
                    row.createCell(++cellNumber).setCellValue(processObjectField(property.getAmountOfRent()));

                    Map<String, String> attachmentPairs = mapToAttachmentCategoryLinkPair(property.getAttachments());

                    for (String header : attachmentHeaders) {
                        row.createCell(++cellNumber)
                                .setCellValue(processObjectField(attachmentPairs.get(header)));
                    }
                }
        );
    }

    private CellStyle getCellBoldStyle(HSSFWorkbook workbook) {
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

    private String processObjectField(Object object) {
        return nonNull(object) ? object.toString() : DEFAULT_CELL_VALUE;
    }

    private Map<String, String> mapToAttachmentCategoryLinkPair(List<AttachmentResponseDto> attachments) {

        return attachments.stream()
                .collect(Collectors.toMap(
                        AttachmentResponseDto::getCategoryName,
                        AttachmentResponseDto::getLink));
    }

}
