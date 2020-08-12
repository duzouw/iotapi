package com.jm.api.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知任务
 * @author duzou
 */
@Accessors(chain = true)
@TableName("iot_inter_notifytask")
@Data
public class NotifyTask implements Serializable {
    private static final long serialVersionUID = 8496204089140852449L;

    @TableId(value = "STA_LOG_ID",type = IdType.AUTO)
    private String taskId;

    private Integer taskStatus;

    private String taskType;

    private LocalDateTime createTime;

    private String taskDesc;
}
