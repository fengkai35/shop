package com.fengkai35.seckill.entity;

public class OrderInfo {
    private long seckillId;
    private long userPhone;
    private String orderID;
    private double price;

    public OrderInfo(long seckillId, long userPhone) {
        this.seckillId = seckillId;
        this.userPhone = userPhone;
        this.orderID = Long.toString(seckillId)+Long.toString(userPhone);
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
