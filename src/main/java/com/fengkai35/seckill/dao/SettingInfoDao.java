package com.fengkai35.seckill.dao;


import com.fengkai35.seckill.entity.SettingInfo;
import org.apache.ibatis.annotations.Param;

public interface SettingInfoDao {
    SettingInfo queryByUsername(@Param("username") String username);
}
