package com.fengkai35.seckill.dao;


import com.fengkai35.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface SeckillDao {

    //减库存
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    Seckill queryById(@Param("seckillId") long seckillId);

    List<Seckill>  queryAll();
    public void seckillByProcedure(Map<String, Object> paramMap);

    int addNumber(@Param("seckillId") long seckillId);

}
