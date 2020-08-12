package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@TableName("iot_cfg_gprscard")
@Data
public class Sim implements Serializable {
    private static final long serialVersionUID = 7659570587162913352L;

    @TableField("IOT_ID")
    private String iotId;

    @NotNull
    @TableField("IOT_MSISDN")
    private String msisdn;

    @NotNull
    @TableField("IOT_IMSI")
    private String imsi;

    @NotNull
    @TableField("IOT_ICCID")
    private String iccid;

}
