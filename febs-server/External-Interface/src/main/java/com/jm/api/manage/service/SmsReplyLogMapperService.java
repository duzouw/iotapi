package com.jm.api.manage.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jm.api.manage.entity.QbReplyToPush;
import com.jm.api.manage.entity.SmsReplyLog;


public interface SmsReplyLogMapperService extends IService<SmsReplyLog> {


    /**
     * D平台回复推送接收
     *
     * @param xml 推送信息
     * @return 成功 true 反之 false
     */
    boolean dReplyToPush(String xml);

    /**
     * 齐犇回复推送接收
     *
     * @param qb 推送信息
     * @return 成功 true 反之 false
     */
    boolean qbReplyToPush(QbReplyToPush qb);

    /**
     * 中景元回复推送接收
     * @param iccid iccid
     * @param taskId 任务id
     * @param message 回复信息
     * @return 成功 true 反之 false
     */
    boolean zjyReplyToPush(String iccid, String taskId, String message);
}
