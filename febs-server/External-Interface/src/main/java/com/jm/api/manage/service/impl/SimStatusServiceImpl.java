package com.jm.api.manage.service.impl;

import cc.mrbird.febs.common.core.utils.FebsUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jm.api.manage.entity.*;
import com.jm.api.manage.mapper.*;
import com.jm.api.manage.service.SimStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class SimStatusServiceImpl extends ServiceImpl<SimStatusMapper, SimStatus> implements SimStatusService {

    private final NotifyTaskMapper notifyTaskMapper;

    private final SimMapper simMapper;

    private final StatusMapper statusMapper;

    private final CorrelationMapper correlationMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertSimStatus(String iccid, String operType) {

        Sim sim = simMapper.selectOne(new QueryWrapper<Sim>().select("IOT_ID").lambda().eq(Sim::getIccid, iccid));

        if (null == sim) {
            log.warn("iccid:{},卡号查询失败", iccid);
            return false;
        }

        Correlation correlation = correlationMapper.selectOne(new QueryWrapper<Correlation>().select("STATUS_ID").lambda().eq(Correlation::getIotId, sim.getIotId()));

        if (null == correlation) {
            log.warn("iot_id:{},卡号查询失败", sim.getIotId());
            return false;
        }

        Status status = statusMapper.selectOne(new QueryWrapper<Status>().lambda().eq(Status::getStatusId, correlation.getStatusId()));

        if (null == status) {
            log.warn("status_id:{},卡号查询失败", correlation.getStatusId());
            return false;
        }

        String type = "2".equals(status.getCardStatus()) ? "1" : "4".equals(status.getCardStatus()) ? "0" : status.getCardStatus();

        if (operType.equals(type))
            return true;


        SimStatus s = new SimStatus();

        s.setStaOld(status.getCardStatus());
        s.setStaNew(operType);

        s.setIotId(sim.getIotId());
        s.setOperUser(String.valueOf(FebsUtil.getCurrentUser().getClientId()));
        s.setStaOperResult(2);
        s.setIsClient(1);
        s.setStaOperType("automatic");
        s.setStaCreateTime(LocalDateTime.now());

        // 通知任务
        if (notifyTaskMapper.insert(new NotifyTask()
                .setTaskStatus(1)
                .setTaskType("RT-S1")
                .setCreateTime(LocalDateTime.now())
                .setTaskDesc("卡状态变更")) <= 0) {
            log.warn("新增通知任务失败！");
            return false;
        }

        return save(s);
    }

    @Override
    public List<String> insertBatchSimStatus(List<String> ids, String operType) {


        List<Sim> sim = simMapper.selectList(new QueryWrapper<Sim>().lambda().in(Sim::getIccid, ids));//.select("IOT_ID","IOT_ICCID")

        if (null == sim || sim.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> iotId = new ArrayList<>();
        sim.forEach(x -> iotId.add(x.getIotId()));
        Map<String, @NotNull String> miotId = sim.stream().collect(Collectors.toMap(Sim::getIotId, Sim::getIccid));

        List<Correlation> correlation = correlationMapper.selectList(new QueryWrapper<Correlation>().select("IOT_ID", "STATUS_ID").lambda().in(Correlation::getIotId, iotId));

        if (null == correlation || correlation.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> statusId = new ArrayList<>();
        correlation.forEach(x -> statusId.add(x.getStatusId()));

        Map<String, String> collect = correlation.stream().collect(Collectors.toMap(Correlation::getStatusId, Correlation::getIotId));

        List<Status> status = statusMapper.selectList(new QueryWrapper<Status>().lambda().in(Status::getStatusId, statusId));//.select("STATUS_ID", "IOT_STATUS")

        if (null == status || status.isEmpty()) {
            return new ArrayList<>();
        }

        List<SimStatus> ls = new ArrayList<>();
        String uId = String.valueOf(FebsUtil.getCurrentUser().getClientId());
        List<String> iccid = new ArrayList<>();

        status.forEach(x -> {
            if (("1".equals(operType) && "2".equals(x.getCardStatus()) || ("0".equals(operType) && "4".equals(x.getCardStatus())))) {
                if (miotId.containsKey(collect.get(x.getStatusId()))){
                    iccid.add(miotId.get(collect.get(x.getStatusId())));
                }
                return;
            }

            if (collect.containsKey(x.getStatusId())) {
                SimStatus s = new SimStatus();

                s.setStaOld(x.getCardStatus());
                s.setStaNew(operType);
                s.setIotId(collect.get(x.getStatusId()));
                s.setOperUser(uId);
                s.setStaOperResult(2);
                s.setIsClient(1);
                s.setStaOperType("automatic");
                s.setStaCreateTime(LocalDateTime.now());

                ls.add(s);

                if (miotId.containsKey(collect.get(x.getStatusId()))){
                    iccid.add(miotId.get(collect.get(x.getStatusId())));
                }
            }
        });


        // 通知任务
        if (notifyTaskMapper.insert(new NotifyTask()
                .setTaskStatus(1)
                .setTaskType("RT-S1")
                .setCreateTime(LocalDateTime.now())
                .setTaskDesc("卡状态变更")) <= 0) {
            log.warn("新增通知任务失败！");
            return new ArrayList<>();
        }

        saveBatch(ls);

        return iccid;
    }
}
