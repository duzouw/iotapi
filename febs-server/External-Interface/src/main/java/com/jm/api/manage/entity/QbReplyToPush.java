package com.jm.api.manage.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class QbReplyToPush implements Serializable {

    private static final long serialVersionUID = -4807727803042258150L;
    private String messageid;
    private String msisdn;
    private String iccid;
    private String content;
    private String reply_time;
}
