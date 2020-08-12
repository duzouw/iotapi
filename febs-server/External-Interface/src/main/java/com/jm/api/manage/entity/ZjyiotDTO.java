package com.jm.api.manage.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZjyiotDTO implements Serializable {

    private static final long serialVersionUID = -7594791281913066492L;

    private String action;
    private String iccid;
    private String orderno;
    private String code;
    private String tip;

}
