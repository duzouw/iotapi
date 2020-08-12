package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("sms_send_log")
@Data
public class SimSend implements Serializable {
    private static final long serialVersionUID = -4142433038655102816L;

    @TableId(value = "DOWN_ID", type = IdType.AUTO)
    private String downId;

    @NotNull
    @TableField(exist = false)
    private String iccid;


    private String downPhone;

    @Size(max = 320)
    @NotNull
    @TableField("DOWN_TEXT")
    private String smsText;

    @Max(2)
    @Min(1)
    @NotNull
    @TableField("DOWN_SMS_CODE")
    private Integer downSmsCode;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime downSubmitTime;

    private String downStatus;

    private String clientId;

    private String taskid;

    private String downNoteSubmitStatus;

    private String errorCause;
}
