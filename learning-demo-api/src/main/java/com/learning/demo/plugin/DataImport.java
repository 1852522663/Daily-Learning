package com.learning.demo.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class DataImport {
    public void importDataFromFile(MultipartFile file) {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<String> columnNames = getColumnNames(sheet);
            Row headerRow = sheet.getRow(0);
            int totalColumns = headerRow.getLastCellNum();
            StringBuilder values = new StringBuilder();
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                values.append("(");
                for (int colIndex = 0; colIndex < totalColumns; colIndex++) {
                    Cell cell = row.getCell(colIndex);
                    values.append(getCellValue(cell)).append(", ");
                }
                values.setLength(values.length() - 2);
                values.append("), ");
            }
            // 移除最后一个多余的逗号和空格
            values.setLength(values.length() - 2);
            StringBuilder sql = new StringBuilder("INSERT INTO your_table (");
            for (int colIndex = 0; colIndex < columnNames.size(); colIndex++) {
                String columnName = columnNames.get(colIndex);
                sql.append(columnName);
                if (colIndex < columnNames.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(") VALUES ").append(values);
            System.out.println(sql);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常情况
        }
    }

//    public static void main(String[] args) throws IOException {
//        DataImport dataImport = new DataImport();
//
//        // 创建模拟的MultipartFile对象
//        Resource resource = new ClassPathResource("页面对应biz列表（不完整）.xlsx");
//        InputStream inputStream = resource.getInputStream();
//        MultipartFile file = new MyCustomMultipartFile("页面对应biz列表（不完整）.xlsx", inputStream);
//        // 调用方法进行数据导入
//        dataImport.importDataFromFile2(file);
//    }

    public void importDataFromFile2(MultipartFile file) {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<String> columnNames = getColumnNames(sheet);
            StringBuilder values = new StringBuilder();
            String previousPageValue = null;
            String previousBizValue = null;
            // 保存页面和业务相同的节点前缀
            List<String> samePageBizNodes = new ArrayList<>();
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                // 页面节点的列索引为0
                Cell pageCell = row.getCell(0);
                // 业务节点的列索引为1
                Cell bizCell = row.getCell(1);
                String pageValue = getCellValue(pageCell);
                String bizValue = getCellValue(bizCell);
                if (pageValue.equals(previousPageValue) && bizValue.equals(previousBizValue)) {
                    // 页面和业务与上一行相同，只需要一个VALUES，将节点前缀以数组形式存储
                    // 获取节点前缀的列索引为2
                    samePageBizNodes.add(getCellValue(row.getCell(2)));
                } else {
                    // 页面和业务与上一行不同，生成一条完整的INSERT语句
                    if (!samePageBizNodes.isEmpty()) {
                        String nodesArray = samePageBizNodes.stream()
                                .map(node -> node.replaceAll("'", "\""))
                                .collect(Collectors.joining(", ", "'[", "]'"));
                        values.append("(").append(previousPageValue).append(", ").append(previousBizValue)
                                .append(", ").append(nodesArray).append("),\n");
                    }
                    previousPageValue = pageValue;
                    previousBizValue = bizValue;
                    samePageBizNodes.clear();
                    samePageBizNodes.add(getCellValue(row.getCell(2))); // 获取节点前缀的列索引为2
                }
            }
            // 添加最后一组相同页面和业务的节点前缀
            if (!samePageBizNodes.isEmpty()) {
                String nodesArray = samePageBizNodes.stream()
                        .map(node -> node.replaceAll("'", "\""))
                        .collect(Collectors.joining(", ", "'[", "]'"));
                values.append("(").append(previousPageValue).append(", ").append(previousBizValue)
                        .append(", ").append(nodesArray).append(")");
            }
            // 移除最后一个多余的逗号
            if (values.length() > 0) {
                values.setLength(values.length() - 1);
            }
            StringBuilder sql = new StringBuilder("INSERT INTO your_table (");
            for (int colIndex = 0; colIndex < columnNames.size(); colIndex++) {
                String columnName = columnNames.get(colIndex);
                sql.append(columnName);
                if (colIndex < columnNames.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(") VALUES ").append(values);
            System.out.println(sql);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常情况
        }
    }


    private List<String> getColumnNames(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        List<String> columnNames = new ArrayList<>();
        for (Cell cell : headerRow) {
            columnNames.add(cell.getStringCellValue());
        }
        return columnNames;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            return "'" + cell.getStringCellValue() + "'";
        } else if (cellType == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cellType == CellType.BLANK) {
            return "";
        } else {
            return "";
        }
    }
}
