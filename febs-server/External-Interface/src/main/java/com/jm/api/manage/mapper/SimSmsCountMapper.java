package com.jm.api.manage.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jm.api.manage.entity.SimSmsCount;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;

public interface SimSmsCountMapper extends BaseMapper<SimSmsCount> {


    List<SimSmsCount> countSms(@Param("ids") List<String> idsm, @Param("kaishi") Date kaishi, @Param("jieshu") Date jieshu);

}
