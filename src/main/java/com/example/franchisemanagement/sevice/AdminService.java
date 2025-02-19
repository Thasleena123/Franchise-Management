package com.example.franchisemanagement.sevice;

import com.example.franchisemanagement.Repository.*;
import com.example.franchisemanagement.entity.*;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private FranchiseStockRepository franchiseStockRepository;
    @Autowired
    private FranchiseRepository franchiseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private CompanyStockRepository companyStockRepository;
    @Autowired
    private Authenticate authenticate;
    @Autowired
    private CompanyPurchaseRepository companyPurchaseRepository;
    @Autowired
    private SupplyRepository supplyRepository;


    public FranchiseEntity addFranchise(FranchiseEntity franchiseEntity) {
        return franchiseRepository.save(franchiseEntity);
    }

    public void deleteFranchase(int franchiseId) {
        franchiseRepository.deleteById(franchiseId);
    }

    public UserEntity createUser(UserEntity userEntity) {
        userEntity.setPassword(authenticate.encodePassword(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    public ProductEntity addProduct(ProductEntity product) {
        return productRepository.save(product); // Save the product to the database
    }

    public ProductEntity updateProduct(int productId, ProductEntity updatedProduct) {
        ProductEntity product = productRepository.findByproductId(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setProductName(updatedProduct.getProductName());
        product.setCategory(updatedProduct.getCategory());
        product.setDistributorPrice(updatedProduct.getDistributorPrice());
        product.setRetailPrice(updatedProduct.getRetailPrice());
        product.setWholesalePrice(updatedProduct.getWholesalePrice());

        return productRepository.save(product);
    }

    @Transactional
    public CompanyStockEntity addProductToCompanyStock(int productId, int quantity) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CompanyStockEntity companyStock = new CompanyStockEntity();

        companyPurchaseRepository.save(new CompanyPurchaseEntity(productId, new Date(System.currentTimeMillis()), quantity));

        companyStock.setProductId(productId);
        companyStock.setQuantity(quantity);
        return companyStockRepository.save(companyStock);
    }


    @Transactional
    public void approveStocKRequest(int RequestId) {
        RequestEntity request = requestRepository.findById(RequestId)
                .orElseThrow(() -> new RuntimeException("stock request not found"));


        request.setStatus("Approved");
        requestRepository.save(request);
        allocateProductToFranchase(request.getFranchiseid(), request.getProductId(), request.getNoOfRequestedProduct());
    }

    @Transactional
    public void rejectStockRequest(int requestId) {
        RequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Stock request not found"));
        if ("Approved".equals(request.getStatus())) {
            throw new RuntimeException("request is already approved so cannot be rejected");
        }
        request.setStatus("Rejected");
        requestRepository.save(request);
    }

    @Transactional
    public void allocateProductToFranchase(int franchiseId, int productId, int quantityToAllocate) {
        CompanyStockEntity companyStockEntity = companyStockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found in company stock"));

        if (companyStockEntity.getQuantity() < quantityToAllocate) {
            throw new RuntimeException("not enough stock in company inventory");
        }
        FranchiseStockEntity franchiseStock = franchiseStockRepository.findByFranchiseIdAndProductId(franchiseId, productId)
                .orElse(null);
        if (franchiseStock != null) {
            franchiseStock.setQuantity(franchiseStock.getQuantity() + quantityToAllocate);
            franchiseStockRepository.save(franchiseStock);
        } else {
            franchiseStock = new FranchiseStockEntity();
            franchiseStock.setFranchiseId(franchiseId);
            franchiseStock.setProductId(productId);
            franchiseStock.setQuantity(quantityToAllocate);

            franchiseStockRepository.save(franchiseStock);

        }
        supplyRepository.save(new SupplyEntity(new Date(System.currentTimeMillis()), productId, quantityToAllocate, franchiseId));

        companyStockEntity.setQuantity(companyStockEntity.getQuantity() - quantityToAllocate);
        companyStockRepository.save(companyStockEntity);

    }

    public ByteArrayInputStream generateCompanyPurchaseReport(Date startDate, Date endDate) throws IOException {
        List<CompanyPurchaseEntity> purchases = companyPurchaseRepository.findByPurchaseDateBetween(startDate, endDate);
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Company Purchase Report");
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "purchase ID", "product ID", "purchase Date", "Quantity"};


            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowNum = 1;
            for (CompanyPurchaseEntity purchase : purchases) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(purchase.getPurchaseId());
                row.createCell(1).setCellValue(purchase.getProductId());
                row.createCell(2).setCellValue(purchase.getPurchaseDate().toString());
                row.createCell(3).setCellValue(purchase.getQuantity());
            }
            Row summaryRow = sheet.createRow(rowNum + 1);
            summaryRow.createCell(0).setCellValue("Total purchases");


            int totalQuality = purchases.stream().mapToInt(CompanyPurchaseEntity::getQuantity).sum();
            summaryRow.createCell(3).setCellValue(totalQuality);
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("failed to generate Exel Report", e);
        }
    }
}



