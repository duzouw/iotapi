package com.jm.api.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jm.api.manage.entity.DiscountInfo;
import com.jm.api.manage.entity.QbReplyToPush;
import com.jm.api.manage.entity.Sim;
import com.jm.api.manage.entity.SmsReplyLog;
import com.jm.api.manage.mapper.DiscountInfoMapper;
import com.jm.api.manage.mapper.SimMapper;
import com.jm.api.manage.mapper.SmsReplyLogMapper;
import com.jm.api.manage.service.SmsReplyLogMapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Service
public class SmsReplyLogMapperServiceImpl extends ServiceImpl<SmsReplyLogMapper, SmsReplyLog> implements SmsReplyLogMapperService {

    private final SimMapper simMapper;
    private final DiscountInfoMapper discountInfoMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean dReplyToPush(String xml) {
        log.info("D平台回复推送xml，{}", xml);

        Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            log.error("xml解析失败，{}", e.getMessage());
            return false;
        }


        List<SmsReplyLog> l = new ArrayList<>();
        List<String> msisdns = new ArrayList<>();

        Element element = document.getRootElement();
        element.elements("deliver").forEach(x -> {
            Element e = (Element) x;

            SmsReplyLog s = new SmsReplyLog();

            s.setUpPhone(e.selectSingleNode("mobile").getText());
            s.setUpText(e.selectSingleNode("content").getText());
            s.setUpReceiveTime(e.selectSingleNode("receivetime").getText());
            s.setUserDownJninupYard(e.selectSingleNode("ac").getText());
            msisdns.add(e.selectSingleNode("mobile").getText());
            l.add(s);
        });

        List<String> ids = new ArrayList<>();

        List<Sim> sims = simMapper.selectList(new QueryWrapper<Sim>().lambda().in(Sim::getMsisdn, msisdns));

        sims.forEach(x -> ids.add(x.getIotId()));

        Map<@NotNull String, String> collectIotId = sims.stream().collect(Collectors.toMap(Sim::getMsisdn, Sim::getIotId));

        if (ids.isEmpty()) {
            log.error("iotId查询为空");
            return false;
        }

        Map<Boolean, List<DiscountInfo>> collect = discountInfoMapper.findClientId(ids).stream().collect(Collectors.partitioningBy(x -> x.getTotal() > 1));

        List<String> iotIds = new ArrayList<>();

        collect.get(true).forEach(x -> iotIds.add(x.getIotId()));

        if (!iotIds.isEmpty()) {
            Map<String, String> topClientId = discountInfoMapper.selectList(new QueryWrapper<DiscountInfo>().lambda().eq(DiscountInfo::getIsTop, "0").in(DiscountInfo::getIotId, iotIds)).stream().collect(Collectors.toMap(DiscountInfo::getIotId, DiscountInfo::getClientId));

            l.forEach(x -> {
                if (collectIotId.containsKey(x.getUpPhone()))
                    if (topClientId.containsKey(collectIotId.get(x.getUpPhone())))
                        x.setClientId(topClientId.get(collectIotId.get(x.getUpPhone())));
            });
        }


        Map<String, String> collectClientId = collect.get(false).stream().collect(Collectors.toMap(DiscountInfo::getIotId, DiscountInfo::getClientId));

        l.forEach(x -> {
            if (collectIotId.containsKey(x.getUpPhone()))
                if (collectClientId.containsKey(collectIotId.get(x.getUpPhone())))
                    x.setClientId(collectClientId.get(collectIotId.get(x.getUpPhone())));
        });

        boolean flag = saveBatch(l);

//        l.clear();
//        msisdns.clear();
//        ids.clear();
//        sims = null;
//        collect.clear();
//        collectIotId.clear();
//        topClientId.clear();
//        collectClientId.clear();

        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean qbReplyToPush(QbReplyToPush qb) {
        log.info("齐犇平台回复推送信息，{}", qb.toString());
        SmsReplyLog s = new SmsReplyLog();
        s.setUpPhone(qb.getMsisdn());
        s.setUpText(qb.getContent());
        s.setUpReceiveTime(qb.getReply_time());
        s.setUserDownJninupYard(qb.getMessageid());

        Sim sim = simMapper.selectOne(new QueryWrapper<Sim>().lambda().eq(Sim::getMsisdn, qb.getMsisdn()));

        if (null == sim) {
            log.error("齐犇回复推送 msisdn号：{}，查询iotId失败", qb.getMsisdn());
            return false;
        }

        if (findClientId(s, sim)) return false;

        return save(s);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean zjyReplyToPush(String iccid, String taskId, String message) {
        log.info("中景元短信回复推送信息,iccid：{}，taskId：{}，message：{}", iccid, taskId, message);

        SmsReplyLog s = new SmsReplyLog();

        s.setUpText(message);
        s.setUpReceiveTime(LocalDateTime.now().toString());
        s.setUserDownJninupYard(taskId);

        Sim sim = simMapper.selectOne(new QueryWrapper<Sim>().lambda().eq(Sim::getIccid, iccid));

        if (null == sim || null == sim.getMsisdn()) {
            log.error("中景元短信回复推送 iccid号：{}，查询iotId失败", iccid);
            return false;
        }

        s.setUpPhone(sim.getMsisdn());

        if (findClientId(s, sim)) return false;


        return save(s);
    }

    private boolean findClientId(SmsReplyLog s, Sim sim) {
        DiscountInfo clientOne = discountInfoMapper.findClientOne(sim.getIotId());

        if (null == clientOne || null == clientOne.getClientId()) {
            log.error("查询关联客户失败：{}", sim.getIotId());
            return true;
        }

        if (clientOne.getTotal() > 1) {
            DiscountInfo discountInfo = discountInfoMapper.selectOne(new QueryWrapper<DiscountInfo>().lambda().eq(DiscountInfo::getIsTop, "0").eq(DiscountInfo::getIotId, sim.getIotId()));
            if (null == discountInfo || null == discountInfo.getClientId()) {
                log.error("查询非顶级关联客户失败：{}", sim.getIotId());
                return true;
            }
            s.setClientId(discountInfo.getClientId());
        } else {
            s.setClientId(clientOne.getClientId());
        }
        return false;
    }
}
