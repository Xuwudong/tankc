package com.senninha.tankc.map.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.map.Direction;
import com.senninha.tankc.map.GridStatus;
import com.senninha.tankc.map.MapHelper;
import com.senninha.tankc.map.message.ReqRunMessage;
import com.senninha.tankc.map.message.ResMapResourceMessage;
import com.senninha.tankc.map.message.ResRunResultMessage;
import com.senninha.tankc.ui.GameData;

import cn.senninha.sserver.client.ClientSession;
import cn.senninha.sserver.lang.dispatch.MessageHandler;
import cn.senninha.sserver.lang.dispatch.MessageInvoke;
import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.message.CmdConstant;

@MessageHandler
public class MapHandler {
	private Logger logger = LoggerFactory.getLogger(MapHandler.class);
	
	@MessageInvoke(cmd = CmdConstant.MAP_RESOURCE_RES)
	public void receiveMap(int sessionId, BaseMessage message) {
		logger.error("收到地图阻挡信息");
		ResMapResourceMessage res = (ResMapResourceMessage) message;
		GameData.getInstance().setMap(res.getList());
		//更新地图
		GameData.getInstance().updateMap();
		
		//测试
		upmove();
		
		
		logger.error("推送行走数据成功");
	}
	
	@MessageInvoke(cmd = CmdConstant.RUN_RES)
	public void receiveRun(int sessionId, BaseMessage message){
		ResRunResultMessage res = (ResRunResultMessage) message;
		GridStatus g = MapHelper.getStatus(sessionId, res, res.getDirection());
		GameData.getInstance().updateMap(res.getX(), res.getY(), g, res.getDirection(), sessionId);
		//刷新UI
		GameData.getInstance().updateMap();
		logger.error("更新{},{}坐标完毕", res.getX(), res.getY());
//		move();
	}
	
	/**
	 * 向下移动，测试用
	 */
	private void upmove() {
		// 尝试移动
		ReqRunMessage req = new ReqRunMessage();
		req.setDirection(Direction.SOUTH.getDirection());
		req.setGridStep((byte) 10);

		ClientSession.getInstance().pushMessage(req);
	}
}
