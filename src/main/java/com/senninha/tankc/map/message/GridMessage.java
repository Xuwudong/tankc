package com.senninha.tankc.map.message;

import cn.senninha.sserver.lang.codec.MessageWrapperAnnotation;
import cn.senninha.sserver.message.CmdConstant;

@MessageWrapperAnnotation(cmd = CmdConstant.GRID)
public class GridMessage {
	private byte x;
	private byte y;
	private int pixelX;
	private int pixelY;
	private byte status;
	private int sessionId;
	public byte getX() {
		return x;
	}
	public void setX(byte x) {
		this.x = x;
	}
	public byte getY() {
		return y;
	}
	public void setY(byte y) {
		this.y = y;
	}
	public int getPixelX() {
		return pixelX;
	}
	public void setPixelX(int pixelX) {
		this.pixelX = pixelX;
	}
	public int getPixelY() {
		return pixelY;
	}
	public void setPixelY(int pixelY) {
		this.pixelY = pixelY;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	public GridMessage() {
		super();
	}
	
	public GridMessage(byte x, byte y, byte status) {
		this.x = x;
		this.y = y;
		this.status = status;
	}
}
