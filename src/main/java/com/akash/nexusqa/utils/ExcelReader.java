package com.akash.nexusqa.utils;

import com.akash.nexusqa.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    private static final Logger logger = LogManager.getLogger(ExcelReader.class);

    /**
     * Reads an Excel sheet into a List of String arrays, one array per data row.
     * Assumes the first row is a header row and skips it.
     */
    public static List<String[]> readSheet(String filePath, String sheetName) {
        List<String[]> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new FrameworkException("Sheet '" + sheetName + "' not found in file: " + filePath);
            }

            int lastRowNum = sheet.getLastRowNum();
            int columnCount = sheet.getRow(0).getLastCellNum();

            for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                String[] rowData = new String[columnCount];
                for (int col = 0; col < columnCount; col++) {
                    Cell cell = row.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[col] = getCellValueAsString(cell);
                }
                rows.add(rowData);
            }

            logger.info("Read {} data rows from sheet '{}' in {}", rows.size(), sheetName, filePath);

        } catch (IOException e) {
            logger.error("Failed to read Excel file: {}", filePath, e);
            throw new FrameworkException("Could not read Excel file: " + filePath, e);
        }

        return rows;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case BLANK -> "";
            default -> "";
        };
    }
}