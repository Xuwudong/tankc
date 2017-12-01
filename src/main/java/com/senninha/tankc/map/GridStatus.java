package com.senninha.tankc.map;

/**
 * 格子的状态
 * @author senninha
 *
 */
public enum GridStatus {
	/** 可走 **/
	CAN_RUN(0),
	
	/** 可以击穿 **/
	CAN_SHOT(1),
	
	/**不可击穿 **/
	CAN_NOT_SHOT(2),
	
	/** 有人在这个格子 **/
	HAS_PLAYER(3),
	
	/**坦克，自己 向上 **/
	SELF_TANK_UP(4),
	
	/** 坦克，自己，向右**/
	SELF_TANK_RIGHT(5),
	
	/**坦克，自己 向下**/
	SELF_TANK_DOWN(6),
	
	/**坦克，自己，向左 **/
	SELF_TANK_LEFT(7),
	
	/**坦克，别人 向上 **/
	OTHER_TANK_UP(8),
	
	/** 坦克，别人，向右**/
	OTHER_TANK_RIGHT(9),
	
	/**坦克，别人 向下**/
	OTHER_TANK_DOWN(10),
	
	/**坦克，别人，向左 **/
	OTHER_TANK_LEFT(11),
	
	/** 炮弹 **/
	SHELLS((byte)12),
	
	/** 炸弹爆炸 **/
	BOOM0((byte) 13),
	
	;
	

	

	private int status;	
	private GridStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
}
