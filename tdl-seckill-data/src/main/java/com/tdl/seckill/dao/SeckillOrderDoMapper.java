package com.tdl.seckill.dao;

import com.tdl.seckill.dos.SeckillOrderDo;
import com.tdl.seckill.dos.SeckillOrderDoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SeckillOrderDoMapper {
    long countByExample(SeckillOrderDoExample example);

    int deleteByExample(SeckillOrderDoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SeckillOrderDo record);

    int insertSelective(SeckillOrderDo record);

    List<SeckillOrderDo> selectByExample(SeckillOrderDoExample example);

    SeckillOrderDo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SeckillOrderDo record, @Param("example") SeckillOrderDoExample example);

    int updateByExample(@Param("record") SeckillOrderDo record, @Param("example") SeckillOrderDoExample example);

    int updateByPrimaryKeySelective(SeckillOrderDo record);

    int updateByPrimaryKey(SeckillOrderDo record);
}