package com.fengkai35.seckill.entity;

import java.util.Date;

public class User {
    public User() {
    }

    public User(long user_id, long userPhone) {
        this.user_id = user_id;
        this.userPhone = userPhone;
    }

    private long user_id;
    private long userPhone;



    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }
}
