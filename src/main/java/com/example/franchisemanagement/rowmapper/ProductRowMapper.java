package com.example.franchisemanagement.rowmapper;

import com.example.franchisemanagement.entity.FranchiseEntity;
import com.example.franchisemanagement.entity.ProductEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

//public class ProductRowMapper implements RowMapper<ProductEntity> {
//    @Override
//    public ProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
//     ProductEntity productEntity = new ProductEntity();
//       productEntity.setProduct_id(rs.getInt("product_id"));
//        productEntity.setProductName(rs.getString("product_name"));
//        productEntity.setCategory(rs.getString("category"));
//        productEntity.setWholesalePrice(rs.getDouble("wholesale_price"));
//        productEntity.setDistributorPrice(rs.getDouble("distributor_price"));
//        productEntity.setRetailPrice(rs.getDouble("retail_price"));
//        return productEntity;
//    }
//
//}


