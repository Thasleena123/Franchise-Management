package com.example.franchisemanagement.rowmapper;

import com.example.franchisemanagement.entity.FranchiseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


//public class FranchiseRowMapper implements RowMapper<FranchiseEntity> {
//    @Override
//    public FranchiseEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
//        FranchiseEntity franchiseEntity = new FranchiseEntity();
//        franchiseEntity.setFranchise_id(rs.getInt("franchise_id"));
//        franchiseEntity.setLocation(rs.getString("location"));
//        return franchiseEntity;
//    }
//
//}
