package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@TableName("sms_reply_log")
@Data
public class SimlatesSms implements Serializable {
    private static final long serialVersionUID = 3443449363484014994L;

    @JsonIgnore
    @TableField("UP_PHONE")
    private String msisdn;

    private String upTest;

    @TableField(exist = false)
    private String iccid;

}
