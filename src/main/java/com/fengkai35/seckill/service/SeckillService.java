package com.fengkai35.seckill.service;

import com.fengkai35.seckill.dto.Exposer;
import com.fengkai35.seckill.dto.SeckillExecution;
import com.fengkai35.seckill.entity.OrderInfo;
import com.fengkai35.seckill.entity.Seckill;
import com.fengkai35.seckill.exception.RepeatKillException;
import com.fengkai35.seckill.exception.SeckillCloseException;
import com.fengkai35.seckill.exception.SeckillException;

import java.util.List;


//从使用者角度设计接口,方法定义粒度，参数，返回类型
public interface SeckillService {
	
	List<Seckill>  getSeckillList();
	
	Seckill getById(long seckillId);
	//输出秒杀开启接口地址
	Exposer exportSeckillUrl(long seckillId);

//	/**
//	 * 执行描述操作
//	 *
//	 * @param seckillId
//	 * @param userPhone
//	 * @param md5
//	 */
//	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)  throws SeckillCloseException, RepeatKillException, SeckillException;
//	  /**
//     * 通过存储过程执行秒杀
//     * @param seckillId
//     * @param userPhone
//     * @param md5
//     */
//    SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5);

	/**
	 * 创建用户
	 * @param userPhone

	 */
    int addUser(Long userPhone);

	SeckillExecution executeOrder(long seckillId, long userPhone, String md5);


	int updatePayState(String order_number);


	int clearOrder();


	int unPaidOrder();
}
    