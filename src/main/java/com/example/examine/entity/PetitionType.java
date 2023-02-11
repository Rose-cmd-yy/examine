package com.example.examine.entity;

import lombok.Data;

@Data
public class PetitionType {
    private long id;
    private String context;
    private long createId;
    private String createTime;
    private long updateId;
    private String updateTime;
    private int isDelete;
}
