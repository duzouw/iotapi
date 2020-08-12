package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("iot_flow_day_log")
@Data
public class SimGprs implements Serializable {
    private static final long serialVersionUID = -8244636207716552550L;

    @JsonIgnore
    @TableField("IOT_MSISDN")
    private String msisdn;

    @TableField(exist = false)
    private String iccid;

    @TableField("DAY_FLOW")
    private String dataAmount;

    @JsonIgnore
    private Date updateTime;
}
