package com.fengkai35.pay.service.impl;

import com.fengkai35.seckill.dao.SettingInfoDao;
import com.fengkai35.seckill.entity.SettingInfo;
import com.fengkai35.pay.service.PayService;


import com.fengkai35.pay.util.AlipayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PayServiceImpl implements PayService {

    private Logger logger=LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SettingInfoDao settingInfoDao;


    @Override
    public int settingInfo() {
        String username = "fengkai35";
        String username_localhost = "fengkai35_localhost";
        SettingInfo settingInfo = settingInfoDao.queryByUsername(username);
        if(settingInfo!=null){
            AlipayConfig.public_ip = settingInfo.getPublicIp();
            AlipayConfig.app_id=settingInfo.getAppId();
            AlipayConfig.alipay_public_key=settingInfo.getAlipayPublicKey();
            AlipayConfig.merchant_private_key=settingInfo.getMerchantPrivateKey();
            AlipayConfig.initURL(AlipayConfig.public_ip);
            return 1;
        }
        return 0;
    }
}
