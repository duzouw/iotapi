package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class SmsReplyLog implements Serializable {
    private static final long serialVersionUID = 4280793998901964095L;

    @TableId(value = "UP_ID",type = IdType.AUTO)
    private String upId;

    private String upPhone;

    private String upText;

    private String upReceiveTime;

    private String clientId;

    private String userDownJninupYard;

}
