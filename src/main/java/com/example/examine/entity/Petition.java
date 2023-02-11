package com.example.examine.entity;

import lombok.Data;

@Data
public class Petition {
    private long id;
    private long detailsId;
    private long resultId;
    private long petitionSourceId;
    private long petitionTypeId;
    private String petitionWay;
    private String petitionTitle;
    private String petitionOid;
    private String petitionSid;
    private int isOpen;
    private String owningSite;
    private String occurAreaCode;
    private String occurAreaAdd;
    private String overTime;
    private String visitorSum;
    private String registerName;
    private String submitTime;
    private String talkName;
    private String talkTime;
    private String managerName;
    private String managerTime;
    private String petitionName;
    private String petitionPhone;
    private String petitionIdCard;
    private int petitionIdCardType;
    private String petitionEmail;
    private String petitionIp;
    private String petitionJobTitle;
    private String petitionContactAddressCode;
    private String petitionContactAddress;
    private String petitionPostCode;
    private String hiddenContent;
    private long departmentId;
    private long handlerId;
    private int handlerStatus;
    private int isTimeOut;
    private long createId;
    private String createTime;
    private long updateId;
    private String updateTime;
    private int isDelete;
    private int count;
}
