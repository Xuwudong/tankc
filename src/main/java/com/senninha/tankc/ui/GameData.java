package com.senninha.tankc.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.senninha.tankc.map.Grid;
import com.senninha.tankc.map.GridStatus;
import com.senninha.tankc.map.MapHelper;
import com.senninha.tankc.map.handler.MapHandler;

import cn.senninha.sserver.client.TankClient;

/**
 * 游戏实体地图数据
 * @author senninha
 *
 */
public class GameData {
	private static GameData instance;
	/** 地图阻挡的数据 **/
	private List<Grid> mapGrids;
	private GameFrame gameFrame;
	private Map<Integer, TankClient> tankContainer;
	private volatile byte direction;
	
	
	private GameData(){
		tankContainer = new HashMap<>();
	}
	
	public static GameData getInstance(){
		if(instance == null){
			synchronized (GameData.class) {
				if(instance == null){
					instance = new GameData();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 更新地图,只是更新地图资源，并不刷新ui
	 * @param x	坐标点
	 * @param y	坐标点
	 * @param status 更新的格子是什么类型的
	 * @return	是否更新成功
	 */
	public boolean updateMap(int x, int y, GridStatus status, int direction, int sessionId){
		if(this.mapGrids == null){
			return false;
		}
		int gridIndex = MapHelper.convertPixelToGridIndex(x, y);
		
		TankClient client = tankContainer.get(sessionId);
		if(client != null){//获取场景中原来是否存在坦克,如果存在，去除这个破坦克
			int oldIndex = MapHelper.convertPixelToGridIndex(client.getX(), client.getY());
			mapGrids.get(oldIndex).setStatus((byte)GridStatus.CAN_RUN.getStatus());
			client.setX(x);
			client.setY(y);
		}else{//否则，把这个坦克加入场景
			client = new TankClient(x, y, direction, sessionId);
			tankContainer.put(sessionId, client);
		}
		mapGrids.get(gridIndex).setStatus((byte)status.getStatus());

		

		return true;
	}
	
	/**
	 * 清理地图信息
	 */
	public void clearMap(){
		this.mapGrids = null;
	}
	
	/**
	 * 游戏开始的时候初始化地图
	 * @param mapGrids
	 */
	public void setMap(List<Grid> mapGrids){
		this.mapGrids = mapGrids;
	}
	
	/**
	 * 仅仅可以被panel调用
	 * @return
	 */
	public List<Grid> getMap(){
		return mapGrids;
	}

	/**
	 * 设置gameFrame，用来更新的
	 * @param gameFrame
	 */
	public void setGameFrame(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
	}
	
	/**
	 * 刷新UI
	 */
	public void updateMap(){
		if(gameFrame != null){
			gameFrame.repaint();
		}
	}

	/**
	 * 当前场景中的坦克
	 * @return
	 */
	public Map<Integer, TankClient> getTankContainer() {
		return tankContainer;
	}

	public void setTankContainer(Map<Integer, TankClient> tankContainer) {
		this.tankContainer = tankContainer;
	}

	/**
	 * 当前坦克的移动方向，这个方法是给推送移动消息给客户端的时候获取的移动方向的，如果当前不需要移动，返回-1
	 * @return
	 */
	public byte getDirection() {
		byte result = direction;
		direction = -1;
		return result;
	}

	/**
	 * 设置下一步应该移动的信息
	 * @param direction
	 */
	public void setDirection(byte direction) {
		this.direction = direction;
	}	
	
	
	
	
}
