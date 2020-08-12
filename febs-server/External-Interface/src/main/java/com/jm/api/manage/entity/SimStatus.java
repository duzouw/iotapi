package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *短信发送任务
 * @author duzou
 */
@TableName("iot_card_status_log")
@Data
public class SimStatus implements Serializable {
    private static final long serialVersionUID = -7395282158549692824L;

    @TableId(value = "STA_LOG_ID",type = IdType.ASSIGN_ID)
    public String staLogId;

    public String iotId;

    public String operUser;

    public Integer staOperResult;

    public String staOld;

    public String staNew;

    public Integer isClient;

    public String staOperType;

    public LocalDateTime staCreateTime;
}