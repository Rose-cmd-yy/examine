package com.example.examine.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.examine.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<UserRole> find();
    public List<Menu> findMenuByUserId(long id);
    public List<Petition> findPetitionByTime(@Param("startDate")String startDate,@Param("endDate")String endDate);
    @DS("db2")
    public Integer insert(Vehicle vehicle);
    @DS("db2")
    public List<Vehicle> findByState(@Param("vehicle")Vehicle vehicle);
    @DS("db2")
    public List<LicenseNumber> findBylicenseNumber();
}
