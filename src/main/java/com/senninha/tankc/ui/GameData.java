package com.senninha.tankc.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.map.Grid;
import com.senninha.tankc.map.GridStatus;
import com.senninha.tankc.map.MapHelper;

import cn.senninha.sserver.client.TankClient;

/**
 * 游戏实体地图数据
 * @author senninha
 *
 */
public class GameData {
	private Logger logger = LoggerFactory.getLogger(GameData.class);
	private static GameData instance;
	/** 地图阻挡的数据 **/
	private List<Grid> mapGrids;
	private GameFrame gameFrame;
	private Map<Integer, TankClient> tankContainer;
	private volatile byte direction;
	private volatile boolean isFire;
	private volatile boolean isInGame;
	

	/**
	 * 获取开火，自动设置开火为false
	 * @return
	 */
	public boolean isFire() {
		boolean res = isFire;
		isFire = false;
		return res;
	}

	/**
	 * 设置开火
	 */
	public void setFire() {
		this.isFire = true;
	}

	private GameData(){
		init();
	}
	
	/**
	 * 开始新游戏的时候才调用
	 */
	public void init() {
		if (tankContainer == null) {
			tankContainer = new HashMap<>();
			this.direction = -1;
		}else {
			logger.debug("GameData已经初始化");
		}
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
	 * 更新地图,只是更新地图资源，并不刷新ui,这个是当某个坦克进入地图的时候必须调用的方法！！！
	 * @param x	坐标点
	 * @param y	坐标点
	 * @param status 更新的格子是什么类型的
	 * @return	是否更新成功
	 */
	public boolean updateMap(int x, int y, GridStatus status, int direction, int sessionId){
		if(this.mapGrids == null){
			return false;
		}
		
//		if(!halfValueCheck(this.mapGrids, x, y)) {
//			return false;
//		}
		
		
		int gridIndex = MapHelper.convertPixelToGridIndex(x, y);
		
		TankClient client = tankContainer.get(sessionId);
		if(client != null){//获取场景中原来是否存在坦克,如果存在，去除这个破坦克
			int oldIndex = MapHelper.convertPixelToGridIndex(client.getX(), client.getY());
			Grid oldGrid = mapGrids.get(oldIndex);
			oldGrid.setPixelX(0);
			oldGrid.setPixelY(0);
			oldGrid.setStatus((byte)GridStatus.CAN_RUN.getStatus());
			
			client.setX(x);
			client.setY(y);
			client.setDirection(direction);
		}else{//否则，把这个坦克加入场景
			client = new TankClient(x, y, direction, sessionId);
			client.setDirection(direction);
			tankContainer.put(sessionId, client);
		}
		
		/** 设置新的格子 **/
		Grid newGrid = mapGrids.get(gridIndex);
		newGrid.setStatus((byte)status.getStatus());;
		newGrid.setPixelX(x);
		newGrid.setPixelY(y);
		
		return true;
	}
	
	/**
	 * 碰撞中值判断，如果不行直接去除这个方法，目前只检测砖头--
	 * @param x 像素点
	 * @param y 像素点
	 * @param gridIndexOfXY 当前像素点代表的格子
	 * @return
	 */
	protected boolean halfValueCheck(List<Grid> grids, int x, int y) {
		/** 分别进行上，下，左，右的碰撞检测 **/
		int halfValue = MapHelper.PER_GRID_PIXEL / 2 - 1;
		int widthMax = MapHelper.PER_GRID_PIXEL * MapHelper.WIDTH_GRIDS - halfValue;
		int heightMax = MapHelper.PER_GRID_PIXEL * MapHelper.HEIGHT_GRIDS - halfValue;
		if(x < halfValue || y < halfValue || x > widthMax || y > heightMax) {	//越界了
			return false;
		}
		
		/** 检测上，右，下，左加上半值是否有砖块 **/
		int temX, temY;
		int temGridIndex;
		
		/** 上检测 **/
		temY = y - halfValue;
		temGridIndex = MapHelper.convertPixelToGridIndex(x, temY);
		if(grids.get(temGridIndex).getStatus() == GridStatus.CAN_NOT_SHOT.getStatus()) {
			return false;
		}
		
		/** 右检测 **/
		temX = x + halfValue;
		temGridIndex = MapHelper.convertPixelToGridIndex(temX, y);
		if(grids.get(temGridIndex).getStatus() == GridStatus.CAN_NOT_SHOT.getStatus()) {
			return false;
		}
		
		/**下检测 **/
		temY = y + halfValue;
		temGridIndex = MapHelper.convertPixelToGridIndex(x, temY);
		if(grids.get(temGridIndex).getStatus() == GridStatus.CAN_NOT_SHOT.getStatus()) {
			return false;
		}
		
		/**左检测 **/
		temX = x - halfValue;
		temGridIndex = MapHelper.convertPixelToGridIndex(temX, y);
		if(grids.get(temGridIndex).getStatus() == GridStatus.CAN_NOT_SHOT.getStatus()) {
			return false;
		}
		
		/** 左上角检测，右上角检测，右下角检测，左下角检测 **/
		
		return true;
		
	}
	
	/**
	 * 更新地图,只是更新地图资源,炮弹进入的时候必须调用这个方法
	 * @param x	坐标点
	 * @param y	坐标点
	 * @param status 更新的格子是什么类型的
	 * @return	是否更新成功
	 */
	public boolean updateMapOfBullet(int x, int y, GridStatus status, int direction, int sessionId){
		if(this.mapGrids == null){
			return false;
		}
		int gridIndex = MapHelper.convertPixelToGridIndex(x, y);
		
		TankClient client = tankContainer.get(sessionId);
		if(client != null){//获取场景中原来是否有子弹
			int oldIndex = MapHelper.convertPixelToGridIndex(client.getX(), client.getY());
			if(mapGrids.get(oldIndex).getStatus() == GridStatus.SHELLS.getStatus()) {//1
				mapGrids.get(oldIndex).setStatus((byte)GridStatus.CAN_RUN.getStatus());
			}
			client.setX(x);
			client.setY(y);
			client.setDirection(direction);
		}else{//否则，把这个子弹加入场景
			client = new TankClient(x, y, direction, sessionId);
			client.setDirection(direction);
			tankContainer.put(sessionId, client);
		}
			Grid grid = mapGrids.get(gridIndex);
			grid.setStatus((byte) status.getStatus());
			//设置像素点
			grid.setPixelX(x);
			grid.setPixelY(y);
		
		return true;
	}
	
	/**
	 * 清理地图信息,所有的信息，包括坦克子弹
	 */
	public void clearMap(){
		this.mapGrids = null;
		this.tankContainer = null;
		this.isInGame = false;
	}
	
	/**
	 * 游戏开始的时候初始化地图
	 * @param mapGrids
	 */
	public void setMap(List<Grid> mapGrids){
		this.mapGrids = mapGrids;
		//优化地图
		MapHelper.optimizeDisplay(mapGrids);
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
	 * 刷新信息
	 */
	public void updateInfo(String text) {
		if(gameFrame != null) {
			gameFrame.printInfo(text);
		}
	}
	
	/**
	 * 刷新顶部标题
	 * @param text
	 */
	public void updateTitle(String text) {
		if(gameFrame != null) {
			gameFrame.setTitle(text);
		}
	}

	/**
	 * 当前场景中的坦克
	 * @return
	 */
	public Map<Integer, TankClient> getTankContainer() {
		return tankContainer;
	}
	
	/**
	 * 根据session获取地图中的某个物体，如果是子弹的话，就是这个子弹唯一的标识id
	 * @param sessionId
	 * @return
	 */
	public TankClient getMapObjectBySessionId(int sessionId){
		return this.tankContainer.get(sessionId);
	}

	public void setTankContainer(Map<Integer, TankClient> tankContainer) {
		this.tankContainer = tankContainer;
	}

	/**
	 * 当前坦克的移动方向，这个方法是给推送移动消息给客户端的时候获取的移动方向的，如果当前不需要移动，返回-1
	 * 只是用来判断当前是否需要推送移动信息给服务端的，不是用来判断当前坦克朝向,判断当前坦克朝向的在 {@linkplain TankClient}
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

	public boolean isInGame() {
		return isInGame;
	}

	public void setInGame(boolean isInGame) {
		this.isInGame = isInGame;
	}	
	
	
	
	
}
