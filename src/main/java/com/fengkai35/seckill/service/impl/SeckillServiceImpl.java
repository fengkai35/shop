package com.fengkai35.seckill.service.impl;

import com.fengkai35.seckill.dao.SeckillDao;
import com.fengkai35.seckill.dao.SuccessKillDao;
import com.fengkai35.seckill.dao.UserDao;
import com.fengkai35.seckill.dao.cache.RedisDao;
import com.fengkai35.seckill.dto.Exposer;
import com.fengkai35.seckill.dto.SeckillExecution;
import com.fengkai35.seckill.entity.OrderInfo;
import com.fengkai35.seckill.entity.Seckill;
import com.fengkai35.seckill.entity.SuccessKill;
import com.fengkai35.seckill.enums.SeckillStatEnum;
import com.fengkai35.seckill.exception.RepeatKillException;
import com.fengkai35.seckill.exception.SeckillCloseException;
import com.fengkai35.seckill.exception.SeckillException;
import com.fengkai35.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.collections.MapUtils;

@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger=LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao  seckillDao;
    @Autowired
    private SuccessKillDao successKillDao;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private UserDao userDao;

    //加盐处理
    private final String slat="xvzbnxsd^&&*)(*()kfmv4165323DGHSBJ";


    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll();
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }


    public Exposer exportSeckillUrl(long seckillId) {

        //优化点：缓存优化
        //超时的基础上去维护一致性（注：假定相应时间不会变化）

        //优化前
//        Seckill seckill = seckillDao.queryById(seckillId);
//        if (seckill == null) {
//            return new Exposer(false, seckillId);
//        }

        //优化后
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //放入redis
                redisDao.putSeckill(seckill);
            }
        }


        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //当前系统时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转换特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

//    /**
//     * @param seckillId
//     * @param userPhone
//     * @param md5
//     * @return
//     * @throws SeckillException
//     * @throws RepeatKillException
//     * @throws SeckillCloseException
//     */
//    //使用声明式事务，发生错误，该方法则回退不执行
//    @Transactional
//    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
//            throws SeckillException, RepeatKillException, SeckillCloseException {
//        if (md5 == null || (!md5.equals(getMD5(seckillId)))) {
//            throw new SeckillException("Seckill data rewrite");
//        }
//        //执行秒杀逻辑：减库存，记录购买行为
//        Date nowTime = new Date();
//        try {
//            //优化前
//            //减库存
////            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
////            if (updateCount <= 0) {
////                //没有更新到记录，秒杀结束
////                throw new SeckillCloseException("Seckill is closed");
////            } else {
////                //记录购买行为
////                int insertCount = successKillDao.insertSuccessKill(seckillId, userPhone);
////                if (insertCount <= 0) {
////                    //重复秒杀
////                    throw new RepeatKillException("Seckill repeated");
////                } else {
////                    //秒杀成功
////                    SuccessKill successKilled = successKillDao.queryByIdWithSeckill(seckillId, userPhone);
////                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
////                }
////            }
//            //优化后
//            //记录购买行为
//            Seckill seckill = seckillDao.queryById(seckillId);
//            String price = seckill.getPrice();
//            long time = System.currentTimeMillis();
//            String order_number = Long.toString(time)+Long.toString(time);
//            int insertCount = successKillDao.insertSuccessKill(seckillId, userPhone,order_number,price);
//            if (insertCount <= 0) {
//                //重复秒杀
//                throw new RepeatKillException("Seckill repeated");
//            } else {
//                //减库存
//                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
//                if (updateCount <= 0) {
//                    //没有更新到记录，秒杀结束
//                    throw new SeckillCloseException("Seckill is closed");
//                } else {
//                    //秒杀成功
//                    SuccessKill successKilled = successKillDao.queryByIdWithSeckill(order_number);
//                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
//
//                }
//            }
//
//
//        } catch (SeckillCloseException e1) {
//            throw e1;
//        } catch (RepeatKillException e2) {
//            throw e2;
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            //所有编译期异常转换为运行时异常
//            throw new SeckillException("Seckill inner error" + e.getMessage());
//        }
//
//    }




    /**
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */

    public SeckillExecution executeOrder(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || (!md5.equals(getMD5(seckillId)))) {
            throw new SeckillException("Seckill data rewrite");
        }
        //执行秒杀逻辑：减库存，记录购买行为
        Date nowTime = new Date();
        try {

            //优化后
            //记录购买行为
            Seckill seckill = seckillDao.queryById(seckillId);
            String price = seckill.getPrice();
            long time = System.currentTimeMillis();
            String order_number = Long.toString(time)+Long.toString(userPhone);
            short state = 0;
            SuccessKill unHandleOrder = successKillDao.queryUnHandleOrder(seckillId, userPhone,state);
            if (unHandleOrder != null) {
                System.out.print("Seckill repeated");
                //重复秒杀
                return new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL, unHandleOrder);
            } else {

                return updateOrder(seckillId,userPhone,order_number,price,nowTime);
            }

        } catch (SeckillCloseException e1) {
            throw e1;
        }  catch (Exception e) {
            logger.error(e.getMessage());
            //所有编译期异常转换为运行时异常
            throw new SeckillException("Seckill inner error" + e.getMessage());
        }

    }
    //使用声明式事务，发生错误，该方法则回退不执行
    @Transactional
    public SeckillExecution updateOrder(long seckillId, long userPhone,
            String order_number,
            String price, Date nowTime){

        int insertCount = successKillDao.insertSuccessKill(seckillId, userPhone,order_number,price);

        int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
        if (updateCount <= 0) {
            //没有更新到记录，秒杀结束
            throw new SeckillCloseException("Seckill is closed");
        } else {
            //下单成功
            SuccessKill successKilled = successKillDao.queryByIdOrderNumber(order_number);
            System.out.print(order_number+"11111111111111111111111" +successKilled.getOrderNumber() );
            return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);

        }
    }

    @Override
    public int updatePayState(String order_number) {
        short state = 1;
        short oldState = 0;
        int result = successKillDao.updateOrderState(order_number,state,oldState);
        if(result==0){
            oldState=-1;
            result = successKillDao.updateOrderState(order_number,state,oldState);
        }
        return result;
    }

    @Override
    public int clearOrder() {
        long seconds = 1*3*60;
        short state = -1;
        List<SuccessKill> orderNumber_list =  successKillDao.getUnpaidOrder(seconds,state);
        for (SuccessKill successKill:orderNumber_list) {

            updateOrderAndSeckill(successKill);
        }
        return orderNumber_list.size();
    }

    //使用声明式事务，发生错误，该方法则回退不执行
    @Transactional
    public void updateOrderAndSeckill(SuccessKill successKill){
        short state = -2;
        short oldState = -1;
        successKillDao.updateOrderState(successKill.getOrderNumber(),state,oldState);
        seckillDao.addNumber(successKill.getSeckillId());
    }

    @Override
    public int unPaidOrder() {
        long seconds = 1*1*60;

        short state = -1;
        short oldState = 0;

        List<SuccessKill> orderNumber_list =  successKillDao.getUnpaidOrder(seconds,oldState);
        for (SuccessKill successKill:orderNumber_list) {

            successKillDao.updateOrderState(successKill.getOrderNumber(),state,oldState);
        }
        return orderNumber_list.size();

    }






    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


//    /**
//     * @param seckillId
//     * @param userPhone
//     * @param md5
//     * @return
//     * @throws SeckillException
//     * @throws RepeatKillException
//     * @throws SeckillCloseException
//     */
////将事务放在数据库存储过程中
//    public SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5) {
//        if (md5 == null || (!md5.equals(getMD5(seckillId)))) {
//            throw new SeckillException("Seckill data rewrite");
//        }
//        Date killTime = new Date();
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("seckillId", seckillId);
//        map.put("phone", userPhone);
//        map.put("killTime", killTime);
//        map.put("result", null);
//        //执行存储过程，result被赋值
//        try {
//            seckillDao.seckillByProcedure(map);
//            //获取result
//            int result = MapUtils.getInteger(map, "result", -2);
//            if (result == 1) {
//                SuccessKill successKilled = successKillDao.queryByIdWithSeckill(seckillId, userPhone);
//                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
//            } else {
//                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
//        }
//    }

    @Override
    public int addUser(Long userPhone) {
        if(userDao.queryByUserPhone(userPhone)==null){
            userDao.addUser(userPhone);
            return 1;
        }
        return 0;
    }



}
