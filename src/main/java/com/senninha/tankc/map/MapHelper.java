package com.senninha.tankc.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.map.message.ReqRunMessage;
import com.senninha.tankc.map.message.ReqShellsMessage;
import com.senninha.tankc.map.message.ResBulletMessage;
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
	private static Random r = new Random();


	public static List<Grid> generateGridRandom() {
		List<Grid> list = new ArrayList<Grid>(TOTAL_GRIDS);
		for (int i = 0; i < TOTAL_GRIDS; i++) {
			if (i % 5 == 0) {
				list.add(new Grid((byte) (i % WIDTH_GRIDS), (byte) (i / WIDTH_GRIDS),
						(byte) (GridStatus.CAN_NOT_SHOT.getStatus())));
				if(r.nextInt(2) == 0){
					list.get(i).setStatus((byte)GridStatus.CAN_RUN.getStatus());
				}
//				if(i < 20) {
//					list.get(i).setStatus((byte)GridStatus.CAN_RUN.getStatus());
//				}
			} else {
				list.add(new Grid((byte) (i % WIDTH_GRIDS), (byte) (i / WIDTH_GRIDS),
						(byte) (GridStatus.CAN_RUN.getStatus())));
			}
//			list.get(i).setStatus(GridStatus.CAN_RUN.getStatus());
		}
		list.get(0).setStatus((byte)GridStatus.CAN_RUN.getStatus()); //设置出生点
		return list;
	}
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
			logger.debug("推送变化位置给服务端");
		}
	}
	
	/**
	 * 根据一系列信息来获取该绘制的坦克状态
	 * @param sessionId
	 * @param res
	 * @param direction
	 * @return
	 */
	public static GridStatus getStatus(int sessionId, ResRunResultMessage res, int direction){
		int base = 0;
		base = sessionId ==  res.getSessionId() ? 4 : 8;
		if(res.getIsAI()) { //如果是AI的话
			base = GridStatus.AI_UP.getStatus() + direction;
		} else {
			base = base + direction;
		}
		GridStatus[] gs = GridStatus.values();
		for(GridStatus gg : gs){
			if(gg.getStatus() == base)
				return gg;
		}
		return null;
	}
	
	/**
	 * 获取子弹的状态
	 * @param id
	 * @param res
	 * @param direction
	 * @return
	 */
	public static GridStatus getBulletStatus(int id, ResBulletMessage res) {
		GridStatus g = null;
		for(GridStatus gg : GridStatus.values()){
			if(gg.getStatus() == res.getStatus()) {
				g = gg;
			}
		}
		return g;
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
	
	/**
	 * 优化显示
	 * @param grids
	 */
	public static void optimizeDisplay(List<Grid> grids) {
		/** 分别是不是没有阻挡
		 * 	1.上边没有的话，y + half，height - half
		 *  2.左边没有的话，x + half, width - half
		 *  3.右边没有的话，width - half
		 *  3.下边没有的话, height - half
		 **/
		
		int temY, temX;
		int x, y;
		int halfValue = MapHelper.PER_GRID_PIXEL / 2;
		int temGridIndex;
		for(int i = 0 ; i < grids.size() ; i++) {
			Grid g = grids.get(i);
			if(g.getStatus() != GridStatus.CAN_NOT_SHOT.getStatus()) {
				continue;
			}
			int[] xy = convertGridIndexToPixel(i);
			x = xy[0]; y = xy[1];
			
			/** 上检测 **/
			temY = y - halfValue * 2;
			temGridIndex = MapHelper.convertPixelToGridIndex(x, temY);
			if(grids.get(temGridIndex).getStatus() != GridStatus.CAN_NOT_SHOT.getStatus()) {
				 g.setPixelY(g.getY() * PER_GRID_PIXEL + halfValue);
				 g.setHeight(PER_GRID_PIXEL - halfValue);
			}
			
			/** 右检测 **/
			temX = x + halfValue * 2;
			temGridIndex = MapHelper.convertPixelToGridIndex(temX, y);
			if(grids.get(temGridIndex).getStatus() != GridStatus.CAN_NOT_SHOT.getStatus()) {
				 g.setWidth(PER_GRID_PIXEL - halfValue);
			}
			
			/**下检测 **/
			temY = y + halfValue * 2;
			temGridIndex = MapHelper.convertPixelToGridIndex(x, temY);
			if(grids.get(temGridIndex).getStatus() != GridStatus.CAN_NOT_SHOT.getStatus()) {
				 g.setHeight(PER_GRID_PIXEL - halfValue);
			}
			
			/**左检测 **/
			temX = x - halfValue * 2;
			temGridIndex = MapHelper.convertPixelToGridIndex(temX, y);
			if(grids.get(temGridIndex).getStatus() == GridStatus.CAN_NOT_SHOT.getStatus()) {
				 g.setPixelX(g.getX() * PER_GRID_PIXEL + halfValue);
				 g.setWidth(PER_GRID_PIXEL - halfValue);
			}
		}
	}
	
	
	public static int[] convertGridIndexToPixel(int gridIndex) {
		int[] xy = new int[2];
		xy[0] = gridIndex % WIDTH_GRIDS * PER_GRID_PIXEL;
		xy[1] = gridIndex / WIDTH_GRIDS * PER_GRID_PIXEL;
		return xy;
	}
}
