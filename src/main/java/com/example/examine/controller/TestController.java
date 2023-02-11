package com.example.examine.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.examine.entity.*;
import com.example.examine.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    UserService service;
    @RequestMapping("/find")
    public List<UserRole> find(){
        return service.find();
    }
    @RequestMapping("/findMenuByUserId")
    public List<Menu> findMenuByUserId(Long id){
        return service.findMenuByUserId(id);
    }
    @RequestMapping("/findPetitionByTime")
    public List<Petition> findPetitionByTime(String startDate, String endDate){
        return service.findPetitionByTime(startDate,endDate);
    }
    @RequestMapping("/insert")
    public Integer insert(Vehicle vehicle){
        return service.insert(vehicle);
    }
    @RequestMapping("/findByState")
    public List<Vehicle> findByState(Vehicle vehicle,int pageNum,int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        return service.findByState(vehicle);
    }
    @RequestMapping("/findBylicenseNumber")
    public String findBylicenseNumber(){
        HashMap<String, Object> data = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<LicenseNumber> list = service.findBylicenseNumber();
        data.put("date",format.format(new Date()));
        data.put("data",list);
        String data_json = JSON.toJSONString(data);
        return data_json;
    }
}
