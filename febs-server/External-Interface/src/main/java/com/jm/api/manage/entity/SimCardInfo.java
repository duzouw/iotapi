package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 码号信息
 */
@TableName("iot_cfg_gprscard")
@Accessors(chain = true)
@Data
public class SimCardInfo implements Serializable{

    private static final long serialVersionUID = 3930023222332412723L;

    @TableField(exist = false)
    private String message;

    @TableField("IOT_MSISDN")
    private String msisdn;

    @TableField("IOT_IMSI")
    private String imsi;

    @TableField("IOT_ICCID")
    private String iccid;
}
