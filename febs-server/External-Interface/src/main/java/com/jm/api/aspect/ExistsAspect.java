package com.jm.api.aspect;

import cc.mrbird.febs.common.core.annotation.Exists;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import com.google.common.collect.ImmutableList;
import com.jm.api.manage.entity.SimCardInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.data.redis.core.BloomOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.Method;

import static cc.mrbird.febs.common.core.BloomFilter.ICCID_FILTER;


/**
 * 接口限流
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ExistsAspect extends BaseAspectSupport {

    private final BloomOperations bloomOperations;


    @Pointcut("@annotation(cc.mrbird.febs.common.core.annotation.Exists)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Object result;

        Object[] args = point.getArgs();

        if (null == args[0]) {
            return point.proceed();
        }

//        Method method = resolveMethod(point);
//        Exists existsAnnotation = method.getAnnotation(Exists.class);
//        if (existsAnnotation.batch()) {

//        } else {
            if (!bloomOperations.exists(ICCID_FILTER, args[0])) {
                return new FebsResponse().data(new SimCardInfo().setMessage("ICCID 号不是所查询的集团下的用户").setIccid((String) args[0]));
            }
//        }


        result = point.proceed();

        try {


            return result;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            String message = throwable.getMessage();

            throw new FebsException(message);
        }
    }

}
