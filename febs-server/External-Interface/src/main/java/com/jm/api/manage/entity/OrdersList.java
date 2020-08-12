package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("fd_orders_list")
@Data
public class OrdersList implements Serializable {
    private static final long serialVersionUID = -7750896414744444727L;

    private String clientId;

    private BigDecimal ordersAmount;

    /**
     * 订单修改时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 支付状态（0 = 未支付，1 = 已支付）
     */
    private Integer paymentStatus;

}
