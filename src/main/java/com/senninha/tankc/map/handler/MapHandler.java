package com.senninha.tankc.map.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.map.Direction;
import com.senninha.tankc.map.message.ReqRunMessage;

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
		//尝试移动
		
		ReqRunMessage req = new ReqRunMessage();
		req.setDirection(Direction.SOUTH.getDirection());
		req.setGridStep((byte)10);
		
		ClientSession.getInstance().pushMessage(req);
		
		logger.error("推送行走数据成功");
	}
}
