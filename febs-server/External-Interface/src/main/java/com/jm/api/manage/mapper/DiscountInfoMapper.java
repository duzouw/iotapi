package com.jm.api.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jm.api.manage.entity.DiscountInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscountInfoMapper extends BaseMapper<DiscountInfo> {

    /**
     * 根据iotId查询客户id
     */
    List<DiscountInfo> findClientId(@Param("ids") List<String> ids);

    /**
     * 根据iotId查询客户id
     */
    DiscountInfo findClientOne(String id);
}
