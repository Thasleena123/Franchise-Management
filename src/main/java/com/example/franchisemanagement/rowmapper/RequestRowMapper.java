//package com.example.franchisemanagement.rowmapper;
//
//import com.example.franchisemanagement.entity.ProductEntity;
//import com.example.franchisemanagement.entity.RequestEntity;
//import org.apache.xmlbeans.impl.xb.xsdschema.Public;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class RequestRowMapper implements RowMapper<RequestEntity> {
//    @Override
//    public RequestEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
//        RequestEntity requestEntity=new RequestEntity();
//        requestEntity.setId(rs.getInt("id"));
//        requestEntity.setStatus(rs.getString(" status"));
//      requestEntity.setProduct_id(rs.getInt("product_id"));
//      requestEntity.setNoOfRequestedProduct(rs.getInt("no_of_requested_product"));
//        return requestEntity;
//    }
//
//    }
//
//
