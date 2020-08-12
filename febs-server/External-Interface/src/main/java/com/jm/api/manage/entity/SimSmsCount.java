package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Accessors(chain = true)
@TableName("sms_reply_log")
@Data
public class SimSmsCount implements Serializable {
    private static final long serialVersionUID = 2355231609386884971L;

    @JsonIgnore
    @TableField("UP_PHONE")
    private String msisdn;

    @JsonIgnore
    @TableField("UP_RECEIVE_TIME")
    private LocalDateTime time;

    @TableField(exist = false)
    private String smsAmount;

    @TableField(exist = false)
    private String iccid;
}
