package com.fengkai35.seckill.dao;


import com.fengkai35.seckill.entity.SuccessKill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuccessKillDao {

    /**
     * 插入购买明细
     *
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone
    ,@Param("orderNumber") String orderNumber, @Param("price") String price);

    /**
     * 根据id查询
     *
     * @param seckillId
     * @return
     */
    SuccessKill  queryUnHandleOrder(@Param("seckillId") long seckillId,
                                    @Param("userPhone") long userPhone, @Param("state") short state);

    SuccessKill  queryByIdOrderNumber(@Param("orderNumber") String orderNumber);


    int updateOrderState(@Param("orderNumber") String orderNumber,@Param("state") short state,@Param("oldState") short oldState);


    List<SuccessKill> getUnpaidOrder(@Param("seconds") long seconds,@Param("state") short state);
}

