package com.senninha.tankc.map;

/**
 * 格子
 * @author senninha
 *
 */
//@MessageWrapperAnnotation(cmd = CmdConstant.GRID)
public class Grid {
	private byte x;
	private byte y;
	private int pixelX;
	private int pixelY;
	private byte status;
	private int sessionId;
	/** 为了前端优化的，不应该加入到协议中 **/
	private int width;
	private int height;
	
	public Grid(byte x, byte y, byte status) {
		super();
		this.x = x;
		this.y = y;
		this.status = status;
		
		/** 前端优化用 **/
		this.width = MapHelper.PER_GRID_PIXEL;
		this.height = MapHelper.PER_GRID_PIXEL;
	}
	
	
	public Grid() {
		super();
	}


	@Override
	public String toString() {
		return "Grid [x=" + x + ", y=" + y + ", pixelX=" + pixelX + ", pixelY=" + pixelY + ", status=" + status
				+ ", sessionId=" + sessionId + "]";
	}
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
	/**
	 * 优化显示的，不应该加入传输协议中
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * 优化显示的，不应该加入传输协议中
	 * @return
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * 优化显示的，不应该加入传输协议中
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * 优化显示的，不应该加入传输协议中
	 * @return
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
