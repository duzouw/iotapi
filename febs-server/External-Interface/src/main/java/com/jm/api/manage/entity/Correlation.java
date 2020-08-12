package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 关联表
 * @author duzou
 */
@TableName("iot_correlation_info")
@Data
public class Correlation implements Serializable {
    private static final long serialVersionUID = -3594889681707892420L;

    private String iotId;

    private String statusId;

    private String pgFlowId;

}
