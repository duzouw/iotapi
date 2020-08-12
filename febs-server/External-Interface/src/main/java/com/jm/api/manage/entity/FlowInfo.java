package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 流量使用信息
 * @author duzou
 */
@TableName("iot_pg_flow_log")
@Data
public class FlowInfo implements Serializable {
    private static final long serialVersionUID = 5230437786777182568L;

    @TableField("PG_FLOW_ID")
    private String offeringId;

    @TableField("PG_NAME")
    private String offeringName;

    @TableField("PG_FLOW")
    private String totalAmount;

    @TableField("SETMEAL_FLOW")
    private String useAmount;

    @TableField("PG_SURPLUS")
    private String remainAmount;

}
