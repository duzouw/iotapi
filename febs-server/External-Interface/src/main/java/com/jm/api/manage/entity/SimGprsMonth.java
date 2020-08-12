package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * gprs 月统计
 */

@TableName("iot_flow_month_log")
@Data
public class SimGprsMonth implements Serializable {
    private static final long serialVersionUID = 4901645382027958699L;

    @TableField("IOT_MSISDN")
    private String msisdn;

    @TableField(exist = false)
    private String iccid;

    @TableField("MONTH_FLOW")
    private String dataAmount;

    private Date updateTime;
}
