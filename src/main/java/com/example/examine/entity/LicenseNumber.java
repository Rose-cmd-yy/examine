package com.example.examine.entity;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
@Data
@JSONType(orders={"province","code","plate","num"})
public class LicenseNumber {
    private String province;
    private String code;
    private String plate;
    private String num;
}
