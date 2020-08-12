package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * 单卡状态查询
 *
 * @author duzou
 */

@TableName("iot_status_info")
@Data
public class Status implements Serializable {
    private static final long serialVersionUID = -382278460808888595L;

    @JsonIgnore
    @TableField("STATUS_ID")
    private String statusId;

    @TableField("IOT_STATUS")
    private String cardStatus;

    @TableField(exist = false)
    private String lastChangeDate;

    @TableField("IOT_ON_OFF_STATUS")
    private String status;

}
