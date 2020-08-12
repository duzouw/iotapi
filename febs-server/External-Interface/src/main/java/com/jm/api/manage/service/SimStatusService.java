package com.jm.api.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jm.api.manage.entity.SimStatus;

import java.util.List;

public interface SimStatusService extends IService<SimStatus> {

    /**
     * 插入一条卡状态变更日志
     *
     * @param iccid
     * @param operType
     * @return 成功 true 反之 false
     */
    boolean insertSimStatus(String iccid, String operType);

    /**
     * 批量插入卡状态变更日志
     *
     * @param ids
     * @param operType
     * @return 成功更新的iccid
     */
    List<String> insertBatchSimStatus(List<String> ids, String operType);
}
