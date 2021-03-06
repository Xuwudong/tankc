package com.senninha.tankc.map.message;

import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.lang.message.Message;
import cn.senninha.sserver.message.CmdConstant;

@Message(cmd = CmdConstant.RUN_RES)
public class ResRunResultMessage extends BaseMessage {
	private int x;
	private int y;
	private int direction;
	private int sessionId;
	/** 0是正常，1是AI **/
	private byte isAI;

	public ResRunResultMessage() {
		super();
	}

	public ResRunResultMessage(int x, int y, int sessionId) {
		super();
		this.x = x;
		this.y = y;
		this.sessionId = sessionId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean getIsAI() {
		return isAI == 1;
	}

	public void setIsAI(byte isAI) {
		this.isAI = isAI;
	}

	@Override
	public String toString() {
		return "ResRunResultMessage [x=" + x + ", y=" + y + ", direction=" + direction + ", sessionId=" + sessionId
				+ "]";
	}

}
