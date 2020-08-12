package com.jm.api.manage.controller;


import cc.mrbird.febs.common.core.annotation.Exists;
import cc.mrbird.febs.common.core.entity.FebsResponse;

import cc.mrbird.febs.common.core.entity.constant.StringConstant;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.jm.api.manage.entity.*;
import com.jm.api.manage.mapper.*;
import com.jm.api.manage.service.SimSendService;
import com.jm.api.manage.service.SimStatusService;
import com.jm.api.manage.service.SmsReplyLogMapperService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BloomOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cc.mrbird.febs.common.core.BloomFilter.*;


/**
 * 接口控制器
 *
 * @author duzou
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class InterfacedController {

    private final BloomOperations bloomOperations;

    private final SimMapper simMapper;

    private final BasicInformationMapper bim;

    private final ChangeHistoryMapper chm;

    private final ImeiMapper imei;

    private final SimCardInfoMapper simCardInfoMapper;

    private final CorrelationMapper correlationMapper;

    private final FlowInfoMapper flowInfoMapper;

    private final FlowCountMapper flowCountMapper;

    private final SimGprsMapper gprsMapper;

    private final SimGprsMonthMapper gprsMonthMapper;

    private final SimSmsCountMapper smsCountMapper;

    private final StatusMapper statusMapper;

    private final SimSendService simSendService;

    private final SimStatusService simStatusService;

    private final OrdersListMapper ordersListMapper;

    private final SimlatesSmsMapper simlatesSmsMapper;




    /**
     * 单卡基本信息查询
     */
    @Exists
    @PreAuthorize("hasAuthority('simBasicInfo:query')")
    @RequestMapping(value = "simBasicInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simBasicInfo(@NotNull String iccid) {
        return new FebsResponse().data(new LambdaQueryChainWrapper<>(bim).eq(BasicInformation::getIccid, iccid).one());
    }


    /**
     * 码号信息批量查询
     */
    @PreAuthorize("hasAuthority('simCardInfoBatch:query')")
    @RequestMapping(value = "simCardInfo/batch", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simCardInfoBatch(Sim s) throws FebsException {
        // 参数校验
        if (null == s.getIccid() && null == s.getImsi() && null == s.getMsisdn()) {
            throw new FebsException("参数不能为空");
        }

        QueryWrapper<SimCardInfo> queryWrapper = new QueryWrapper<>();

        // 判断是否有查询条件
        boolean isQuery = false;

        List<SimCardInfo> error = new ArrayList<>();


        if (StringUtils.isNotBlank(s.getIccid())) {
            List<String> l = new ArrayList<>();
            List<String> strings = Arrays.asList(s.getIccid().split(StringConstant.COMMA));
            List<Boolean> booleans = Arrays.asList(bloomOperations.existsMulti(ICCID_FILTER, strings));

            for (int i = 0; i < booleans.size(); i++) {
                if (booleans.get(i)) {
                    l.add(strings.get(i));
                    isQuery = true;
                } else {
                    error.add(new SimCardInfo().setMessage("ICCID 号不是所查询的集团下的用户").setIccid(strings.get(i)));
                }
            }

            if (!l.isEmpty()) {
                queryWrapper.lambda().or().in(SimCardInfo::getIccid, l);
            }

            l = null;
            strings = null;
            booleans = null;
        }

        if (StringUtils.isNotBlank(s.getMsisdn())) {
            List<String> l = new ArrayList<>();
            List<String> strings = Arrays.asList(s.getMsisdn().split(StringConstant.COMMA));
            List<Boolean> booleans = Arrays.asList(bloomOperations.existsMulti(MSISDN_FILTER, strings));

            for (int i = 0; i < booleans.size(); i++) {
                if (booleans.get(i)) {
                    l.add(strings.get(i));
                    isQuery = true;
                } else {
                    error.add(new SimCardInfo().setMessage("MSISDN 号不是所查询的集团下的用户").setMsisdn(strings.get(i)));
                }
            }
            if (!l.isEmpty()) {
                queryWrapper.lambda().or().in(SimCardInfo::getMsisdn, l);
            }

            l = null;
            strings = null;
            booleans = null;
        }

        if (StringUtils.isNotBlank(s.getImsi())) {
            List<String> l = new ArrayList<>();
            List<String> strings = Arrays.asList(s.getImsi().split(StringConstant.COMMA));
            List<Boolean> booleans = Arrays.asList(bloomOperations.existsMulti(IMSI_FILTER, strings));

            for (int i = 0; i < booleans.size(); i++) {
                if (booleans.get(i)) {
                    l.add(strings.get(i));
                    isQuery = true;
                } else {
                    error.add(new SimCardInfo().setMessage("IMSI 号不是所查询的集团下的用户").setImsi(strings.get(i)));
                }
            }
            if (!l.isEmpty()) {
                queryWrapper.lambda().or().in(SimCardInfo::getImsi, l);
            }

            l = null;
            strings = null;
            booleans = null;
        }


        List<SimCardInfo> simCardInfos = new ArrayList<>();

        // 没有查询条件禁止查询
        if (isQuery) {
            simCardInfos = simCardInfoMapper.selectList(queryWrapper);
            simCardInfos.forEach(x -> x.setMessage("成功"));
        }

        Map<String, Object> m = new HashMap<>(2);
        m.put("succeed", simCardInfos);
        m.put("error", error);

        simCardInfos = null;
        error = null;

        return new FebsResponse().data(m);
    }


    /**
     * 单卡状态变更历史
     */
    @Exists
    @PreAuthorize("hasAuthority('simChangeHistory:query')")
    @RequestMapping(value = "simChangeHistory", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simChangeHistory(@NotNull String iccid) {
        return new FebsResponse().data(new LambdaQueryChainWrapper<>(chm)
                .eq(ChangeHistory::getIotId, simMapper.selectOne(new QueryWrapper<Sim>()
                        .select("IOT_ID").lambda()
                        .eq(Sim::getIccid, iccid))
                        .getIotId())
                .list());
    }

    /**
     * 单卡绑定IMEI查询
     */
    @Exists
    @PreAuthorize("hasAuthority('simImei:query')")
    @RequestMapping(value = "simImei", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simImei(@NotNull String iccid) {
        return new FebsResponse().data(new LambdaQueryChainWrapper<>(imei).eq(Imei::getIccid, iccid).one());
    }


    /**
     * 单卡状态查询
     */
    @Exists
    @PreAuthorize("hasAuthority('simStatus:query')")
    @RequestMapping(value = "simStatus", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simStatus(@NotNull String iccid) {

        String id = simMapper.selectOne(new QueryWrapper<Sim>().select("IOT_ID").lambda().eq(Sim::getIccid, iccid)).getIotId();

        Correlation correlation = new LambdaQueryChainWrapper<>(correlationMapper).eq(Correlation::getIotId, id).one();

        List<ChangeHistory> l = new LambdaQueryChainWrapper<>(chm).eq(ChangeHistory::getIotId, id).orderByDesc(ChangeHistory::getChangeDate).list();

        if (null != correlation && null != l && !l.isEmpty()) {
            Status status = new LambdaQueryChainWrapper<>(statusMapper).eq(Status::getStatusId, correlation.getStatusId()).one();
            l.stream().sorted(Comparator.comparing(ChangeHistory::getChangeDate).reversed()).limit(1).forEach(x -> status.setLastChangeDate(x.getChangeDate()));

            id = null;
            correlation = null;
            l = null;
            return new FebsResponse().data(status);
        } else {
            return new FebsResponse().data(null);
        }
    }

    /**
     * 单卡本月套餐内流量使用量实时查询
     */
    @Exists
    @PreAuthorize("hasAuthority('simDataMargin:query')")
    @RequestMapping(value = "simDataMargin", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simDataMargin(@NotNull String iccid) {

        Correlation correlation = correlationMapper.selectOne(new QueryWrapper<Correlation>().select("PG_FLOW_ID").lambda()
                .eq(Correlation::getIotId, simMapper.selectOne(new QueryWrapper<Sim>().select("IOT_ID").lambda().eq(Sim::getIccid, iccid)).getIotId()));

        if (null != correlation) {
            FlowInfo one = new LambdaQueryChainWrapper<>(flowInfoMapper).eq(FlowInfo::getOfferingId, correlation.getPgFlowId()).one();

            correlation = null;
            return new FebsResponse().data(one);
        } else {
            return new FebsResponse().data(null);
        }
    }

    /**
     * 单卡本月流量累计使用量查询
     */
    @Exists
    @PreAuthorize("hasAuthority('simDataUsage:query')")
    @RequestMapping(value = "simDataUsage", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simDataUsage(@NotNull String iccid) {

        Correlation correlation = correlationMapper.selectOne(new QueryWrapper<Correlation>().select("PG_FLOW_ID").lambda()
                .eq(Correlation::getIotId, simMapper.selectOne(new QueryWrapper<Sim>().select("IOT_ID").lambda().eq(Sim::getIccid, iccid)).getIotId()));

        if (null != correlation) {
            FlowCount one = new LambdaQueryChainWrapper<>(flowCountMapper).eq(FlowCount::getOfferingId, correlation.getPgFlowId()).one();
            correlation = null;
            return new FebsResponse().data(one);
        } else {
            return new FebsResponse().data(null);
        }
    }

    /**
     * 物联卡单日 GPRS 流量使用量批量查询
     */
    @PreAuthorize("hasAuthority('simDataUsageDaily:query')")
    @RequestMapping(value = "simDataUsageDaily/batch", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simDataUsageDailyBatch(@NotNull String iccids,
                                               @NotNull @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "日期格式错误") String queryDate) {

        Date date = DateUtil.parse(queryDate, "yyyyMMdd");

        List<String> ids = Arrays.asList(iccids.split(StringConstant.COMMA));

        // 验证是否归我所属
        List<Boolean> booleans = Arrays.asList(bloomOperations.existsMulti(ICCID_FILTER, ids));

        List<SimCardInfo> error = new ArrayList<>();
        List<String> liccid = new ArrayList<>();

        for (int i = 0; i < booleans.size(); i++) {
            if (booleans.get(i)) {
                liccid.add(ids.get(i));
            } else {
                error.add(new SimCardInfo().setMessage("ICCID 号不是所查询的集团下的用户").setIccid(ids.get(i)));
            }
        }

        ids = null;
        booleans = null;


        Map<String, Object> m = new HashMap<>(2);

        // 查询msisdn
        if (!liccid.isEmpty()) {
            List<String> list = new ArrayList<>();
            List<Sim> ls = new LambdaQueryChainWrapper<>(simMapper).in(Sim::getIccid, liccid).list();
            ls.forEach(x -> list.add(x.getMsisdn()));
            Map<@NotNull String, @NotNull String> collect = ls.stream().collect(Collectors.toMap(Sim::getMsisdn, Sim::getIccid));

            if (!list.isEmpty()) {
                List<SimGprs> lsg = new LambdaQueryChainWrapper<>(gprsMapper)
                        .ge(SimGprs::getUpdateTime, date)
                        .le(SimGprs::getUpdateTime, DateUtil.endOfDay(date))
                        .in(SimGprs::getMsisdn, list).list();

                lsg.forEach(x -> {
                    if (collect.containsKey(x.getMsisdn())) {
                        x.setIccid(collect.get(x.getMsisdn()));
                    }
                });

                m.put("succeed", lsg);

                liccid = null;
                ls = null;
//                list.clear();
//                collect.clear();
                lsg = null;

            } else {
                m.put("succeed", null);
            }
        } else {
            m.put("succeed", null);
        }

        m.put("error", error);

        error = null;
        date = null;
        return new FebsResponse().data(m);
    }

    /**
     * 物联卡单月 GPRS 流量使用量批量查询
     */
    @PreAuthorize("hasAuthority('simDataUsageMonthly:query')")
    @RequestMapping(value = "simDataUsageMonthly/batch", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simDataUsageMonthlyBatch(@NotNull String iccids,
                                                 @NotNull @Pattern(regexp = "^\\d{4}\\d{2}$", message = "日期格式错误") String queryDate) {
        // 格式化时间
        StringBuilder sb = new StringBuilder(queryDate);
        sb.insert(4, "-");

        List<String> ids = Arrays.asList(iccids.split(StringConstant.COMMA));

        // 验证是否归我所属
        List<Boolean> booleans = Arrays.asList(bloomOperations.existsMulti(ICCID_FILTER, ids));

        List<SimCardInfo> error = new ArrayList<>();
        List<String> liccid = new ArrayList<>();

        for (int i = 0; i < booleans.size(); i++) {
            if (booleans.get(i)) {
                liccid.add(ids.get(i));
            } else {
                error.add(new SimCardInfo().setMessage("ICCID 号不是所查询的集团下的用户").setIccid(ids.get(i)));
            }
        }

        ids = null;
        booleans = null;

        Map<String, Object> m = new HashMap<>(2);
        // 查询msisdn
        if (!iccids.isEmpty()) {
            List<String> list = new ArrayList<>();
            List<Sim> ls = new LambdaQueryChainWrapper<>(simMapper).in(Sim::getIccid, liccid).list();
            ls.forEach(x -> list.add(x.getMsisdn()));
            Map<@NotNull String, @NotNull String> collect = ls.stream().collect(Collectors.toMap(Sim::getMsisdn, Sim::getIccid));

            if (!list.isEmpty()) {
                List<SimGprsMonth> lsgm = new LambdaQueryChainWrapper<>(gprsMonthMapper)
                        .eq(SimGprsMonth::getUpdateTime, sb.toString())
                        .in(SimGprsMonth::getMsisdn, list).list();

                lsgm.forEach(x -> {
                    if (collect.containsKey(x.getMsisdn())) {
                        x.setIccid(collect.get(x.getMsisdn()));
                    }
                });

                m.put("succeed", lsgm);

//                list.clear();
//                collect.clear();
                ls = null;
                lsgm = null;
            } else {
                m.put("succeed", null);
            }
        } else {
            m.put("succeed", null);
        }
        m.put("error", error);
        error = null;
        sb = null;
        return new FebsResponse().data(m);
    }

    /**
     * 物联卡单日短信使用量批量查询
     */
    @PreAuthorize("hasAuthority('simSmsUsageDailyBatch:query')")
    @RequestMapping(value = "simSmsUsageDaily/batch", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simSmsUsageDailyBatch(@NotNull String iccids,
                                              @NotNull @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "日期格式错误") String queryDate) {
        Date date = DateUtil.parse(queryDate, "yyyyMMdd");

        List<String> ids = Arrays.asList(iccids.split(StringConstant.COMMA));

        // 验证是否归我所属
        List<Boolean> booleans = Arrays.asList(bloomOperations.existsMulti(ICCID_FILTER, ids));

        List<SimCardInfo> error = new ArrayList<>();
        List<String> liccid = new ArrayList<>();

        for (int i = 0; i < booleans.size(); i++) {
            if (booleans.get(i)) {
                liccid.add(ids.get(i));
            } else {
                error.add(new SimCardInfo().setMessage("ICCID 号不是所查询的集团下的用户").setIccid(ids.get(i)));
            }
        }

        ids = null;
        booleans = null;

        Map<String, Object> m = new HashMap<>(2);

        // 查询msisdn
        if (!liccid.isEmpty()) {
            List<String> list = new ArrayList<>();
            List<Sim> ls = new LambdaQueryChainWrapper<>(simMapper).in(Sim::getIccid, liccid).list();
            ls.forEach(x -> list.add(x.getMsisdn()));
            Map<@NotNull String, @NotNull String> collect = ls.stream().collect(Collectors.toMap(Sim::getMsisdn, Sim::getIccid));

            if (!list.isEmpty()) {

                List<SimSmsCount> simSmsCounts = smsCountMapper.countSms(list, date, DateUtil.endOfDay(date));

                simSmsCounts.forEach(x -> {
                    if (collect.containsKey(x.getMsisdn())) {
                        x.setIccid(collect.get(x.getMsisdn()));
                    }
                });

                m.put("succeed", simSmsCounts);

//                collect.clear();
//                list.clear();
                ls = null;
                simSmsCounts = null;
            } else {
                m.put("succeed", null);
            }
        } else {
            m.put("succeed", null);
        }

        m.put("error", error);

        error = null;
        date = null;

        return new FebsResponse().data(m);
    }

    /**
     * 物联卡单月短信使用量批量查询
     */
    @PreAuthorize("hasAuthority('simSmsUsageMonthlyBatch:query')")
    @RequestMapping(value = "simSmsUsageMonthly/batch", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simSmsUsageMonthlyBatch(@NotNull String iccids,
                                                @NotNull @Pattern(regexp = "^\\d{4}\\d{2}$", message = "日期格式错误") String queryDate) {
        Date date = DateUtil.parse(queryDate + "01", "yyyyMMdd");

        List<String> ids = Arrays.asList(iccids.split(StringConstant.COMMA));

        // 验证是否归我所属
        List<Boolean> booleans = Arrays.asList(bloomOperations.existsMulti(ICCID_FILTER, ids));

        List<SimCardInfo> error = new ArrayList<>();
        List<String> liccid = new ArrayList<>();

        for (int i = 0; i < booleans.size(); i++) {
            if (booleans.get(i)) {
                liccid.add(ids.get(i));
            } else {
                error.add(new SimCardInfo().setMessage("ICCID 号不是所查询的集团下的用户").setIccid(ids.get(i)));
            }
        }

        ids = null;
        booleans = null;


        Map<String, Object> m = new HashMap<>(2);

        // 查询msisdn
        if (!liccid.isEmpty()) {
            List<String> list = new ArrayList<>();
            List<Sim> ls = new LambdaQueryChainWrapper<>(simMapper).in(Sim::getIccid, liccid).list();
            ls.forEach(x -> list.add(x.getMsisdn()));
            Map<@NotNull String, @NotNull String> collect = ls.stream().collect(Collectors.toMap(Sim::getMsisdn, Sim::getIccid));

            if (!list.isEmpty()) {
                List<SimSmsCount> simSmsCounts = smsCountMapper.countSms(list, DateUtil.beginOfMonth(date), DateUtil.endOfMonth(date));

                simSmsCounts.forEach(x -> {
                    if (collect.containsKey(x.getMsisdn())) {
                        x.setIccid(collect.get(x.getMsisdn()));
                    }
                });

                m.put("succeed", simSmsCounts);

//                list.clear();
//                collect.clear();
                ls = null;
                simSmsCounts = null;
            } else {
                m.put("succeed", null);
            }
        } else {
            m.put("succeed", null);
        }

        m.put("error", error);

        error = null;
        date = null;

        return new FebsResponse().data(m);
    }


    /**
     * 单卡本月短信累计使用量查询
     */
    @Exists
    @PreAuthorize("hasAuthority('simSmsUsage:query')")
    @RequestMapping(value = "simSmsUsage", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simSmsUsage(@NotNull String iccid) {

        Sim one = new LambdaQueryChainWrapper<>(simMapper).eq(Sim::getIccid, iccid).one();

        Map<String, Integer> m = new HashMap<>(1);
        m.put("smsAmount", new LambdaQueryChainWrapper<>(smsCountMapper)
                .ge(SimSmsCount::getTime, DateUtil.beginOfMonth(new Date()))
                .le(SimSmsCount::getTime, DateUtil.endOfMonth(new Date()))
                .eq(SimSmsCount::getMsisdn, one.getMsisdn()).count());

        one = null;
        return new FebsResponse().data(m);
    }

    /**
     * 集团客户账单实时查询
     */
    @PreAuthorize("hasAuthority('ecBill:query')")
    @RequestMapping(value = "ecBill", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse ecBill(@NotNull @Pattern(regexp = "^\\d{4}\\d{2}$", message = "日期格式错误") String queryDate) {

        Date date = DateUtil.parse(queryDate + "01", "yyyyMMdd");

        List<OrdersList> ordersLists = ordersListMapper.selectList(new QueryWrapper<OrdersList>().select("orders_amount").lambda()
                .eq(OrdersList::getClientId, FebsUtil.getCurrentUser().getClientId())
                .eq(OrdersList::getPaymentStatus, "1")
                .ge(OrdersList::getUpdateTime, DateUtil.beginOfMonth(date))
                .le(OrdersList::getUpdateTime, DateUtil.endOfMonth(date)));

        Optional<BigDecimal> reduce = ordersLists.stream().map(OrdersList::getOrdersAmount).reduce(BigDecimal::add);

        Map<String, BigDecimal> m = new HashMap<>(1);
        m.put("invoiceAmount", reduce.orElse(BigDecimal.ZERO));

        date = null;
        ordersLists = null;
        return new FebsResponse().data(m);
    }

    /**
     * 单卡开关机状态实时查询
     */
    @Exists
    @PreAuthorize("hasAuthority('onOffStatus:query')")
    @RequestMapping(value = "onOffStatus", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse onOffStatus(@NotNull String iccid) {

        Sim sim = simMapper.selectOne(new QueryWrapper<Sim>().select("IOT_ID").lambda().eq(Sim::getIccid, iccid));

        if (null == sim)
            return new FebsResponse().data(new SimCardInfo().setMessage("ICCID 号不是所查询的集团下的用户").setIccid(iccid));

        String id = sim.getIotId();

        Correlation correlation = new LambdaQueryChainWrapper<>(correlationMapper).eq(Correlation::getIotId, id).one();

        if (null != correlation) {
            Status status = new LambdaQueryChainWrapper<>(statusMapper).eq(Status::getStatusId, correlation.getStatusId()).one();
            Map<String, String> m = new HashMap<>(1);
            m.put("status", status.getStatus());

            id = null;
            correlation = null;
            status = null;
            return new FebsResponse().data(m);
        } else {
            return new FebsResponse().data(null);
        }
    }

    /**
     * 单卡状态变更
     */
    @Exists
    @PreAuthorize("hasAuthority('simStatus:update')")
    @RequestMapping(value = "simStatus/change", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simStatus(@NotNull String iccid, @NotNull @Pattern(regexp = "^[0-1]$", message = "状态格式错误") String operType) {
        return simStatusService.insertSimStatus(iccid, operType) ?
                new FebsResponse().data(new SimCardInfo().setMessage("成功").setIccid(iccid)) : new FebsResponse().data(new SimCardInfo().setMessage("失败").setIccid(iccid));
    }

    /**
     * 批量卡状态变更
     */
    @PreAuthorize("hasAuthority('simStatusBatch:update')")
    @RequestMapping(value = "simStatus/batch", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simStatusBatch(@NotNull String iccids, @NotNull @Pattern(regexp = "^[0-1]$", message = "状态格式错误") String operType) {
        List<String> ids = Arrays.asList(iccids.split(StringConstant.COMMA));

        // 验证是否归我所属
        List<Boolean> booleans = Arrays.asList(bloomOperations.existsMulti(ICCID_FILTER, ids));

        List<SimCardInfo> error = new ArrayList<>();
        List<String> liccid = new ArrayList<>();

        for (int i = 0; i < booleans.size(); i++) {
            if (booleans.get(i)) {
                liccid.add(ids.get(i));
            } else {
                error.add(new SimCardInfo().setMessage("ICCID 号不是所查询的集团下的用户").setIccid(ids.get(i)));
            }
        }


        Map<String, Object> m = new HashMap<>(2);

        List<String> strings = simStatusService.insertBatchSimStatus(liccid, operType);

        List<SimCardInfo> succeed = new ArrayList<>();
        liccid.forEach(x -> {
            if (strings.contains(x)) {
                succeed.add(new SimCardInfo().setIccid(x).setMessage("成功"));
            } else {
                error.add(new SimCardInfo().setIccid(x).setMessage("失败"));
            }
        });

        m.put("succeed", succeed);
        m.put("error", error);

        ids = null;
        booleans = null;
        liccid = null;
//        strings.clear();
//        succeed.clear();

        return new FebsResponse().data(m);
    }

    /**
     * 物联卡终端控制下行短信（短信发送）
     */
    @PreAuthorize("hasAuthority('simMtSms:operate')")
    @RequestMapping(value = "simMtSms", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simMtSms(@Valid SimSend s) {
        if (!exists(s.getIccid())) {
            return new FebsResponse().data(new SimCardInfo().setMessage("ICCID 号不是所查询的集团下的用户").setIccid(s.getIccid()));
        }
        Map<String, Integer> m = new HashMap<>(1);
        m.put("smsSendStatus", simSendService.insertSms(s) ? 0 : 1);
        return new FebsResponse().data(m);
    }

    /**
     * 物联卡终端控制上行短信记录批量查询
     */
    @PreAuthorize("hasAuthority('simMoSmsBatch:query')")
    @RequestMapping(value = "simMoSms/batch", method = {RequestMethod.GET, RequestMethod.POST})
    public FebsResponse simMoSmsBatch(@NotNull String iccids) {
        List<String> ids = Arrays.asList(iccids.split(StringConstant.COMMA));

        // 验证是否归我所属
        List<Boolean> booleans = Arrays.asList(bloomOperations.existsMulti(ICCID_FILTER, ids));

        List<SimCardInfo> error = new ArrayList<>();
        List<String> liccid = new ArrayList<>();

        for (int i = 0; i < booleans.size(); i++) {
            if (booleans.get(i)) {
                liccid.add(ids.get(i));
            } else {
                error.add(new SimCardInfo().setMessage("ICCID 号不是所查询的集团下的用户").setIccid(ids.get(i)));
            }
        }

        ids = null;
        booleans = null;

        Map<String, Object> m = new HashMap<>(2);

        // 查询msisdn
        if (!liccid.isEmpty()) {
            List<String> list = new ArrayList<>();
            List<Sim> ls = new LambdaQueryChainWrapper<>(simMapper).in(Sim::getIccid, liccid).list();

            if (null != ls && !ls.isEmpty()) {
                ls.forEach(x -> list.add(x.getMsisdn()));
                Map<@NotNull String, @NotNull String> collect = ls.stream().collect(Collectors.toMap(Sim::getMsisdn, Sim::getIccid));

                List<SimlatesSms> succeed = new ArrayList<>();

                Map<String, String> sl = simlatesSmsMapper.latestSms(list).stream().collect(Collectors.toMap(SimlatesSms::getMsisdn, SimlatesSms::getUpTest));

                list.forEach(x -> succeed.add(new SimlatesSms().setMsisdn(x).setIccid(collect.get(x)).setUpTest(sl.getOrDefault(x, null))));

                m.put("succeed", succeed);

//                list.clear();
//                collect.clear();
//                sl.clear();
//                succeed.clear();
                ls = null;
            } else {
                m.put("succeed", null);
            }
        } else {
            m.put("succeed", null);
        }

        m.put("error", error);
        error = null;
        return new FebsResponse().data(m);
    }


    public void aaa() {

        // 1.创建布隆过滤器
        bloomOperations.createFilter("iccid-filter", 0.00001, 2000000);
        bloomOperations.createFilter("imsi-filter", 0.00001, 2000000);
        bloomOperations.createFilter("msisdn-filter", 0.00001, 2000000);

//        // 2.添加一个元素
//        Boolean foo = bloomOperations.add(key, "foo");
//        log.info("test add result: {}", foo);

        List<String> ls = new ArrayList<>();
        List<String> ls1 = new ArrayList<>();
        List<String> ls2 = new ArrayList<>();
        simMapper.selectList(null).forEach(x -> {
            ls.add(x.getIccid());
            ls1.add(x.getImsi());
            ls2.add(x.getMsisdn());
        });

        bloomOperations.addMulti("iccid-filter", ls);


        bloomOperations.addMulti("imsi-filter", ls1);


        bloomOperations.addMulti("msisdn-filter", ls2);

//        log.info("test addMulti result: {}", Arrays.toString(addMulti));
//
//        // 4.校验一个元素是否存在
//        Boolean exists = bloomOperations.exists(key, "foo");
//        log.info("test exists result: {}", exists);
//
//        // 5.批量校验元素是否存在
//        Boolean[] existsMulti = bloomOperations.existsMulti(key, "foo", "foo1");
//        log.info("test existsMulti result: {}", Arrays.toString(existsMulti));
//
//        // 6.删除布隆过滤器
//        Boolean delete = bloomOperations.delete(key);
//        log.info("test delete result: {}", delete);
    }

    /**
     * 检查一个元素是否存在容器中
     *
     * @param iccid 元素
     * @return 存在true 反之 false
     */
    private boolean exists(String iccid) {
        return bloomOperations.exists(ICCID_FILTER, iccid);
    }


}
