package com.jm.api.manage.service.impl;

import cc.mrbird.febs.common.core.utils.FebsUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jm.api.manage.entity.QbStatusPush;
import com.jm.api.manage.entity.Sim;
import com.jm.api.manage.entity.SimSend;
import com.jm.api.manage.mapper.SimMapper;
import com.jm.api.manage.mapper.SimSendMapper;
import com.jm.api.manage.service.SimSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Service
public class SimSendServiceImpl extends ServiceImpl<SimSendMapper, SimSend> implements SimSendService {

    private final SimMapper simMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertSms(SimSend s) {

        Sim one = simMapper.selectOne(new QueryWrapper<Sim>().lambda().eq(Sim::getIccid, s.getIccid()));

        if (null == one) {
            log.error("iccid:{}，卡号信息查询失败", s.getIccid());
            return false;
        }

        s.setDownPhone(one.getMsisdn());
        s.setDownSubmitTime(LocalDateTime.now());
        s.setDownStatus("0");
        s.setClientId(String.valueOf(FebsUtil.getCurrentUser().getClientId()));
        return save(s);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean dStatusPush(String xml) {
        log.info("D平台状态推送xml，{}", xml);

        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("xml解析失败，{}", e.getMessage());
            return false;
        }

        Element element = document.getRootElement();

        List<SimSend> l = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        element.elements("report").forEach(x -> {
            Element e = (Element) x;

            SimSend simSend = new SimSend();
            simSend.setDownNoteSubmitStatus("0".equals(e.selectSingleNode("status").getText()) ? "1" : "2");
            simSend.setTaskid(e.selectSingleNode("taskid").getText());
            simSend.setErrorCause(e.selectSingleNode("errcode").getText());
            ids.add(e.selectSingleNode("taskid").getText());
            l.add(simSend);
        });

        if (ids.isEmpty()) {
            log.error("TaskId为空");
            return false;
        }


        Map<String, String> collect = baseMapper.selectList(new QueryWrapper<SimSend>().select("DOWN_ID", "TASKID").lambda().in(SimSend::getTaskid, ids)).stream().collect(Collectors.toMap(SimSend::getTaskid, SimSend::getDownId));

        if (collect.isEmpty()) {
            log.error("TaskId不存在");
            return false;
        }


        l.forEach(x -> {
            if (collect.containsKey(x.getTaskid()))
                x.setDownId(collect.get(x.getTaskid()));
        });

        boolean flag = updateBatchById(l);

//        l.clear();
//        ids.clear();
//        collect.clear();

        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean qbStatusPush(QbStatusPush qb) {
        log.info("齐犇短信状态推送信息，{}", qb.toString());

        String status = "1";

        if("MI:0001".equals(qb.getSubmitstate()) || "DB:0140".equals(qb.getSubmitstate())) {
            status = "2";
        }

        return lambdaUpdate().eq(SimSend::getTaskid,qb.getMessageid())
                .set(SimSend::getDownNoteSubmitStatus,status)
                .set(SimSend::getErrorCause,qb.getSubmitstate()).update();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean zjyStatusPush(String taskId) {
        log.info("中景元短信状态推送信息，taskId：{}", taskId);
        return lambdaUpdate().eq(SimSend::getTaskid,taskId).set(SimSend::getDownNoteSubmitStatus,"1").update();
    }

}