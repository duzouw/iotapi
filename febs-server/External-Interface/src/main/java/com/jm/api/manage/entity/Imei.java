package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import java.io.Serializable;

/**
 * 单卡绑定IMEI
 *
 * @author duzou
 */
@TableName("iot_cfg_gprscard")
@Data
public class Imei implements Serializable {
    private static final long serialVersionUID = -798077623832471580L;

    @JsonIgnore
    @TableField("IOT_MSISDN")
    private String msisdn;

    @JsonIgnore
    @TableField("IOT_IMSI")
    private String imsi;

    @JsonIgnore
    @TableField("IOT_ICCID")
    private String iccid;

    @TableField("IOT_IMEI")
    private String imei;
}
