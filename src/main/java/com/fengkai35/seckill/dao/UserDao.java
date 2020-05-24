package com.fengkai35.seckill.dao;



import com.fengkai35.seckill.entity.User;
import org.apache.ibatis.annotations.Param;




public interface UserDao {

    //减库存
    int addUser(@Param("userPhone") long userPhone);

    User queryByUserPhone(@Param("userPhone") long userPhone);


}
