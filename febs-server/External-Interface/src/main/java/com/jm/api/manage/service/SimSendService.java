package com.jm.api.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jm.api.manage.entity.QbStatusPush;
import com.jm.api.manage.entity.SimSend;

public interface SimSendService extends IService<SimSend> {

    /**
     * 插入短信下行任务表
     *
     * @param s 短信类
     * @return 成功 true 反之 false
     */
    boolean insertSms(SimSend s);


    /**
     * D平台状态推送接收
     * @param xml 推送信息
     * @return 成功 true 反之 false
     */
    boolean dStatusPush(String xml);

    /**
     * 齐犇状态推送接收
     * @param qb 推送信息
     * @return 成功 true 反之 false
     */
    boolean qbStatusPush(QbStatusPush qb);

    /**
     * 中景元状态推送接收
     * @param taskId 推送信息
     * @return 成功 true 反之 false
     */
    boolean zjyStatusPush(String taskId);

}
