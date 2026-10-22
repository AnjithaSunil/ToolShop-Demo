package utilsPackage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility 
{
	 private XSSFWorkbook wb;
	    private XSSFSheet sheet;
	    private final String filePath;

	    public ExcelUtility(String fileName, String sheetName)
	            throws IOException {

	        filePath = System.getProperty("user.dir")
	                + "/src/test/resources/" + fileName;

	        try (FileInputStream fis = new FileInputStream(filePath)) {
	            wb = new XSSFWorkbook(fis);
	        }

	        sheet = wb.getSheet(sheetName);

	        if (sheet == null) {
	            wb.close();
	            throw new IOException(
	                    "Sheet not found: " + sheetName);
	        }
	    }

	    // Read 12 input columns and append the Excel row index.
	    public Object[][] getExcelData(int totalColumns) {

	        List<Object[]> dataList = new ArrayList<>();
	        DataFormatter formatter = new DataFormatter();

	        // Row 0 contains the headers.
	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

	            Row row = sheet.getRow(i);

	            if (row == null || isEmptyRow(row, totalColumns, formatter)) {
	                continue;
	            }

	            // 12 input values + 1 row number.
	            Object[] data = new Object[totalColumns + 1];

	            for (int j = 0; j < totalColumns; j++) {

	                Cell cell = row.getCell(j);

	                if (cell == null) {
	                    data[j] = "";
	                } else if (j == 2 && DateUtil.isCellDateFormatted(cell)) {

	                    // Convert Excel date to yyyy-MM-dd.
	                    LocalDate date = cell.getDateCellValue()
	                            .toInstant()
	                            .atZone(ZoneId.systemDefault())
	                            .toLocalDate();

	                    data[j] = date.format(
	                            DateTimeFormatter.ISO_LOCAL_DATE);

	                } else {
	                    data[j] = formatter.formatCellValue(cell).trim();
	                }
	            }

	            // Keep the actual zero-based sheet row index.
	            // Excel row 2 has index 1.
	            data[totalColumns] = i;

	            dataList.add(data);
	        }

	        return dataList.toArray(new Object[0][]);
	    }

	    private boolean isEmptyRow(
	            Row row, int totalColumns, DataFormatter formatter) {

	        for (int j = 0; j < totalColumns; j++) {

	            Cell cell = row.getCell(j);

	            if (cell != null
	                    && !formatter.formatCellValue(cell).trim().isEmpty()) {
	                return false;
	            }
	        }

	        return true;
	    }

	    // Write only to the column titled "Actual Result".
	    public void writeResult(int rowNo, String actualResult)
	            throws IOException {

	        Row headerRow = sheet.getRow(0);

	        if (headerRow == null) {
	            throw new IOException("Header row is missing.");
	        }

	        int actualResultColumn = -1;

	        for (int i = 0; i < headerRow.getLastCellNum(); i++) {

	            Cell cell = headerRow.getCell(i);

	            if (cell != null
	                    && formatterValue(cell).equalsIgnoreCase("Actual Result")) {
	                actualResultColumn = i;
	                break;
	            }
	        }

	        if (actualResultColumn == -1) {
	            throw new IOException(
	                    "'Actual Result' column not found in Excel.");
	        }

	        Row row = sheet.getRow(rowNo);

	        if (row == null) {
	            row = sheet.createRow(rowNo);
	        }

	        Cell resultCell = row.getCell(actualResultColumn);

	        if (resultCell == null) {
	            resultCell = row.createCell(actualResultColumn);
	        }

	        resultCell.setCellValue(actualResult == null ? "" : actualResult);

	        // Save changes to the same workbook.
	        try (FileOutputStream fos = new FileOutputStream(filePath)) {
	            wb.write(fos);
	            fos.flush();
	        }
	    }

	    private String formatterValue(Cell cell) {
	        return new DataFormatter().formatCellValue(cell).trim();
	    }

	    public void closeExcel() throws IOException {
	        if (wb != null) {
	            wb.close();
	            wb = null;
	            sheet = null;
	        }
	    }

}
