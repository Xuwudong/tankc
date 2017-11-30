package com.senninha.tankc.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.map.message.ReqRunMessage;
import com.senninha.tankc.map.message.ReqShellsMessage;
import com.senninha.tankc.map.message.ResRunResultMessage;
import com.senninha.tankc.ui.GameData;

import cn.senninha.sserver.client.ClientSession;
import cn.senninha.sserver.client.TankClient;

/**
 * Map帮助类
 * 
 * @author senninha
 *
 */
public class MapHelper {
	
	private static Logger logger = LoggerFactory.getLogger(MapHelper.class);

	public static final int WIDTH_GRIDS = 20; // x方向的格子数
	public static final int HEIGHT_GRIDS = 15;
	public static final int TOTAL_GRIDS = WIDTH_GRIDS * HEIGHT_GRIDS;

	public static final int PER_GRID_PIXEL = 40; // 每个格子的像素
//	private static Random r = new Random();


	/**
	 * 根据像素返回这个点所占据的格子下标，因为格子是list储存的。。
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static int convertPixelToGridIndex(int x, int y) {
		if(x >= MapHelper.WIDTH_GRIDS * MapHelper.PER_GRID_PIXEL){
			x = x -1;
		}
		x = x / MapHelper.PER_GRID_PIXEL;
		y = y / MapHelper.PER_GRID_PIXEL;

		return y * MapHelper.WIDTH_GRIDS + x;
	}
	
	/**
	 * 根据GameData里的direction来发送走动信息
	 */
	public static void move(){
		byte direction = GameData.getInstance().getDirection();
		if(direction != Direction.DONT_MOVE.getDirection()){
			ReqRunMessage req = new ReqRunMessage();
			req.setDirection(direction);
			req.setGridStep((byte) 20);
			ClientSession.getInstance().pushMessage(req);
			System.out.println("推送变化位置给服务端");
		}
	}
	
	/**
	 * 根据一系列信息来获取该绘制的坦克状态(包括那些炮弹)
	 * @param sessionId
	 * @param res
	 * @param direction
	 * @return
	 */
	public static GridStatus getStatus(int sessionId, ResRunResultMessage res, int direction){
		int base = 0;
		base = sessionId ==  res.getSessionId() ? 4 : 8;
		base = base + direction;
		GridStatus[] gs = GridStatus.values();
		for(GridStatus gg : gs){
			if(gg.getStatus() == base)
				return gg;
		}
		return null;
	}
	
	/**
	 * 发送开火信息
	 */
	public static void pushFireMessage(){
		boolean isFire = GameData.getInstance().isFire();
		if(isFire){//开火
			ReqShellsMessage req = new ReqShellsMessage();
			int sessionId = ClientSession.getInstance().getSessionId();
			req.setSourceSessionId(sessionId);
			TankClient tank = GameData.getInstance().getMapObjectBySessionId(sessionId);
			if(tank != null){
				req.setX(tank.getX());
				req.setY(tank.getY());
				req.setDirection((byte)tank.getDirection());
				ClientSession.getInstance().pushMessage(req);
				logger.error("推送开火信息成功");
			}else{
				logger.error("地图中无法获取想开炮的客户端{}", sessionId);
			}
		}
	}
}
