package com.jm.api.manage.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * 单卡状态变更历史
 * @author duzou
 */

@TableName("iot_card_status_history_log")
@Data
public class ChangeHistory implements Serializable {
    private static final long serialVersionUID = -3837400632140084437L;

    @JsonIgnore
    @TableField("IOT_ID")
    private String iotId;

    @TableField("OLD_STATUS")
    private String descStatus;

    @TableField("NEW_STATUS")
    private String targetStatus;

    @TableField("CHANGE_TIME")
    private String changeDate;
}
