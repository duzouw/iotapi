package com.jm.api.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jm.api.manage.entity.SimlatesSms;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SimlatesSmsMapper extends BaseMapper<SimlatesSms> {

    List<SimlatesSms> latestSms(@Param("ids") List<String> ids);
}
