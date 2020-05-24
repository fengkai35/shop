package com.fengkai35.seckill.dto;

import com.fengkai35.seckill.entity.SuccessKill;
import com.fengkai35.seckill.enums.SeckillStatEnum;

public class SeckillExecution {
	
	private long seckillId;
	
	private int state;
	
	private String stateInfo;
	
	private SuccessKill successKill;

	public SeckillExecution(long seckillId, SeckillStatEnum statEnum,
			SuccessKill successKill) {
		super();
		this.seckillId = seckillId;
		this.state = statEnum.getState();
		this.stateInfo = statEnum.getStateInfo();
		this.successKill = successKill;
	}

	public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
		super();
		this.seckillId = seckillId;
		this.state = statEnum.getState();
		this.stateInfo = statEnum.getStateInfo();
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public SuccessKill getSuccessKill() {
		return successKill;
	}

	public void setSuccessKill(SuccessKill successKill) {
		this.successKill = successKill;
	}

	@Override
	public String toString() {
		return "SeckillExecution{" +
				"seckillId=" + seckillId +
				", state=" + state +
				", stateInfo='" + stateInfo + '\'' +
				", successKill=" + successKill +
				'}';
	}
}
