package com.example.franchisemanagement.sevice;

import com.example.franchisemanagement.Repository.*;
import com.example.franchisemanagement.entity.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Service
public class FranchiseAdmin {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FranchiseRepository franchiseRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private FranchiseStockRepository franchiseStockRepository;
    @Autowired
    private CompanyStockRepository companyStockRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private Authenticate authenticate;
    @Autowired
    private OrderRepository orderRepository;

    public UserEntity createEmployeeUser(UserEntity userEntity) {
        userEntity.setPassword(authenticate.encodePassword(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    public void deleteEmployeeUser(int userId) {
        userRepository.deleteById(userId);

    }

    public RequestEntity requestNewStock(int productId, int quantityRequested, String sessionId) {
     ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        RequestEntity request = new RequestEntity();
        request.setStatus("Pending");
        request.setProductId(productId);
        request.setNoOfRequestedProduct(quantityRequested);
        int userId = sessionRepository.findBySessionId(sessionId).getUserId();
        UserEntity user = userRepository.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        request.setFranchiseid(user.getFranchiseId());
        return requestRepository.save(request);
    }

    public ByteArrayInputStream generateFranchiseSalesReport(int franchiseId, Date startDate, Date endDate) {
        List<OrderEntity> orders = orderRepository.findByFranchiseIdAndSaleDateBetween(franchiseId, startDate, endDate);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Franchise sales Report");
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Order ID",
                    "Product ID",
                    "Customer Name",
                    "Quantity",
                    "Total Price",
                    "Sale Date"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            double totalSales = 0;
            int totalQuantity = 0;

            for (OrderEntity order : orders) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getProductId());
                row.createCell(2).setCellValue(order.getCustomerName());
                row.createCell(3).setCellValue(order.getQuantity());
                row.createCell(4).setCellValue(order.getTotalPrice());
                row.createCell(5).setCellValue(order.getSaleDate().toString());

                totalSales += order.getTotalPrice();
                totalQuantity += order.getQuantity();
            }
            Row summaryRow1 = sheet.createRow(rowNum + 1);
            summaryRow1.createCell(0).setCellValue("Total Sales Amount:");
            summaryRow1.createCell(4).setCellValue(totalSales);

            Row summaryRow2 = sheet.createRow(rowNum + 2);
            summaryRow2.createCell(0).setCellValue("Total Quantity Sold:");
            summaryRow2.createCell(3).setCellValue(totalQuantity);

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

}

