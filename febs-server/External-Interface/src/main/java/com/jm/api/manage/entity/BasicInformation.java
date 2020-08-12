package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 单卡基本信息
 * @author duzou
 */

@TableName("iot_cfg_gprscard")
@Data
public class BasicInformation implements Serializable {
    private static final long serialVersionUID = 8381161361941253912L;

    @TableField("IOT_MSISDN")
    @NotNull
    private String msisdn;

    @TableField("IOT_IMSI")
    @NotNull
    private String imsi;

    @TableField("IOT_ICCID")
    @NotNull
    private String iccid;

    @TableField("ACTIVATION_TIME")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activeDate;

    @TableField("ADD_TIME")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime openDate;

    @TableField("EXPIRE_TIME")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireDate;

}
