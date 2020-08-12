package com.jm.api.manage.controller;

import com.jm.api.manage.entity.QbReplyToPush;
import com.jm.api.manage.entity.QbStatusPush;
import com.jm.api.manage.entity.ZjyiotDTO;
import com.jm.api.manage.entity.ZjyiotR;
import com.jm.api.manage.service.SimSendService;
import com.jm.api.manage.service.SmsReplyLogMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信回推接口
 *
 * @author duzou
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("sms")
public class SmsController {
    private final SimSendService simSendService;
    private final SmsReplyLogMapperService smsReplyLogMapperService;

    /**
     * D平台状态推送接收
     *
     * @param xml 推送信息
     * @return 成功 ok 反之 失败
     */
    @PostMapping("dStatusPush")
    public String dStatusPush(@RequestBody(required = false) String xml) {
        if (null == xml)
            return "参数不能为空";
        return simSendService.dStatusPush(xml) ? "ok" : "失败";
    }

    /**
     * D平台回复推送接收
     *
     * @param xml 推送信息
     * @return 成功 ok 反之 失败
     */
    @PostMapping("dReplyToPush")
    public String dReplyToPush(@RequestBody(required = false) String xml) {
        if (null == xml)
            return "参数不能为空";
        return smsReplyLogMapperService.dReplyToPush(xml) ? "ok" : "失败";
    }

    /**
     * 齐犇回复推送接收
     *
     * @param qb 推送信息
     * @return 成功 0 反之 -1
     */
    @PostMapping("qbReplyToPush")
    public String qbReplyToPush(@RequestBody(required = false) QbReplyToPush qb) {
        if (null == qb || null == qb.getMessageid() || null == qb.getMsisdn() || null == qb.getIccid() || null == qb.getContent() || null == qb.getReply_time())
            return "参数不能为空";
        return smsReplyLogMapperService.qbReplyToPush(qb) ? "0" : "-1";
    }

    /**
     * 齐犇状态推送接收
     *
     * @param qb 推送信息
     * @return 成功 0 反之 -1
     */
    @PostMapping("qbStatusPush")
    public String qbStatusPush(@RequestBody(required = false) QbStatusPush qb) {
        if (null == qb || null == qb.getMessageid() || null == qb.getMsisdn() || null == qb.getIccid() || null == qb.getSubmitstate() || null == qb.getRemark())
            return "参数不能为空";
        return simSendService.qbStatusPush(qb) ? "0" : "-1";
    }


    /**
     * 中景元短信状态、回复推送接收
     *
     * @param z 中景元参数
     * @return 中景元返回类型
     */
    @PostMapping("zjyiotPushBack")
    public ZjyiotR zjyiotPushBack(ZjyiotDTO z) {
        if (null == z || null == z.getAction() || null == z.getCode() || null == z.getOrderno() || null == z.getIccid() || null == z.getTip())
            return new ZjyiotR().setStatus("0").setCode("90").setTip("必选参数不能为空").setData(null);

        if ("6".equals(z.getAction())) {
            if ("1".equals(z.getCode())) {
                return simSendService.zjyStatusPush(z.getOrderno()) ? new ZjyiotR().setStatus("1").setCode("100").setTip("受理成功").setData(null) : new ZjyiotR().setStatus("0").setCode("105").setTip("执行失败").setData(null);
            } else if ("2".equals(z.getCode())) {
                return smsReplyLogMapperService.zjyReplyToPush(z.getIccid(), z.getOrderno(), z.getTip()) ? new ZjyiotR().setStatus("1").setCode("100").setTip("受理成功").setData(null) : new ZjyiotR().setStatus("0").setCode("105").setTip("执行失败").setData(null);
            }
        }

        return new ZjyiotR().setStatus("0").setCode("105").setTip("执行失败").setData(null);
    }

}
