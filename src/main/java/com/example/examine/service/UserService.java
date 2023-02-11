package com.example.examine.service;


import com.example.examine.dao.UserMapper;
import com.example.examine.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Autowired(required=false)
    private UserMapper userMapper;
    public List<UserRole> find(){
      return userMapper.find();
  }
    public List<Menu> findMenuByUserId(long id){
        return userMapper.findMenuByUserId(id);
    }
    public List<Petition> findPetitionByTime(String startDate,String endDate){return userMapper.findPetitionByTime(startDate,endDate);}
    public Integer insert(Vehicle vehicle){
        return userMapper.insert(vehicle);
    }
    public List<Vehicle> findByState(Vehicle vehicle){
        return userMapper.findByState(vehicle);
    }
    public List<LicenseNumber> findBylicenseNumber(){
        return userMapper.findBylicenseNumber();
    }

}
