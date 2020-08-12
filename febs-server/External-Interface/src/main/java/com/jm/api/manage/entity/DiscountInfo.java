package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("iot_discount_info")
@Data
public class DiscountInfo implements Serializable {

    private static final long serialVersionUID = -5536305052417259402L;

    private String clientId;

    private String iotId;

    private String isTop;

    @TableField(exist = false)
    private Integer total;
}
