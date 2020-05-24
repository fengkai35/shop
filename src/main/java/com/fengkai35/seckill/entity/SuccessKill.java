package com.fengkai35.seckill.entity;


import java.util.Date;

public class SuccessKill {

    private long seckillId;
    private long userPhone;
    private short state;




    private String orderNumber;
    private String price;

    private Date createTime;

    private Seckill seckill;


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
    public short getState() {
        return state;
    }
    public void setState(short state) {
        this.state = state;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Seckill getSeckill() {
        return seckill;
    }
    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }


    @Override
    public String toString() {
        return "SuccessKill{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", orderNumber='" + orderNumber + '\'' +
                ", price='" + price + '\'' +
                ", createTime=" + createTime +
                ", seckill=" + seckill +
                '}';
    }
}
