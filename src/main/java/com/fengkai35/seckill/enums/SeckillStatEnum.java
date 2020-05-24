package com.fengkai35.seckill.enums;

//常量数据字典
public enum  SeckillStatEnum {
	SUCCESS(1,"下单成功"),
	END(0,"秒杀结束"),
	REPEAT_KILL(-1,"还有未完成的订单"),
	INNER_ERROR(-2,"系统异常"),
	DATA_REWRITE(-3,"数据篡改");
	
	private int state;
	private String stateInfo;
	
	private SeckillStatEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
	
	public static SeckillStatEnum stateOf(int index){
		for (SeckillStatEnum state : values()) {
			if(state.getState()==index){
				return state;
			}
		}
		return null;
	}

}
