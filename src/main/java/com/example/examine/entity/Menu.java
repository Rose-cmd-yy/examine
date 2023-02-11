package com.example.examine.entity;

import lombok.Data;

@Data
public class Menu {
    private long id;
    private String menuName;
    private String menuCode;
    private long parentId;
    private int nodeType;
    private String iconUrl;
    private int sort;
    private String linkUrl;
    private long createId;
    private String createTime;
    private long updateId;
    private String updateTime;
    private int isDelete;
}
