package cn.senninha.sserver.client;

/**
 * 地图里的坦克
 * @author senninha
 *
 */
public class TankClient {
	private int x;	//x像素点
	private int y;	//y像素点
	private int direction;	//方向
	private int sessionId;
	
	public TankClient(int x, int y, int direction, int sessionId) {
		super();
		this.x = x;
		this.y = y;
		this.direction = direction;
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
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	
	
	
}
