package com.jm.api.manage.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class QbStatusPush implements Serializable {
    private static final long serialVersionUID = -3297507995670364867L;

    private String msisdn;
    private String messageid;
    private String content;
    private String submitstate;
    private String remark;
    private String iccid;
}
