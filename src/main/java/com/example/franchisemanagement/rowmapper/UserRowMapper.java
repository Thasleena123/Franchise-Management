//package com.example.franchisemanagement.rowmapper;
//
//import com.example.franchisemanagement.entity.UserEntity;
//import com.example.franchisemanagement.enums.Role;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class UserRowMapper implements RowMapper<UserEntity> {
//    @Override
//    public UserEntity mapRow(ResultSet rs,int rowNum) throws SQLException{
//        UserEntity userEntity=new UserEntity();
//        userEntity.setId(rs.getInt("id"));
//        userEntity.setName(rs.getString("name"));
//        userEntity.setPassword(rs.getString("password"));
//        userEntity.setRole(Role.valueOf(rs.getString("role")));
//        userEntity.setFranchise_id(rs.getInt("franchise_id"));
//
//        return userEntity;
//    }
//}
