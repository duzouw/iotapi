package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author duzou
 */
@TableName("iot_pg_flow_log")
@Data
public class FlowCount implements Serializable {
    private static final long serialVersionUID = -3947315509742710714L;
    @JsonIgnore
    @TableField("PG_FLOW_ID")
    private String offeringId;
    @TableField("PG_FLOW")
    private String dataAmount;
}
