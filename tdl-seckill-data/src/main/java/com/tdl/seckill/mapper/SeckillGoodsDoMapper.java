package com.tdl.seckill.mapper;

import com.tdl.seckill.dos.SeckillGoodsDo;
import com.tdl.seckill.dos.SeckillGoodsDoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SeckillGoodsDoMapper {
    long countByExample(SeckillGoodsDoExample example);

    int deleteByExample(SeckillGoodsDoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SeckillGoodsDo record);

    int insertSelective(SeckillGoodsDo record);

    List<SeckillGoodsDo> selectByExample(SeckillGoodsDoExample example);

    SeckillGoodsDo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SeckillGoodsDo record, @Param("example") SeckillGoodsDoExample example);

    int updateByExample(@Param("record") SeckillGoodsDo record, @Param("example") SeckillGoodsDoExample example);

    int updateByPrimaryKeySelective(SeckillGoodsDo record);

    int updateByPrimaryKey(SeckillGoodsDo record);

    int updateOptimistic(SeckillGoodsDo seckillGoodsDo);
}