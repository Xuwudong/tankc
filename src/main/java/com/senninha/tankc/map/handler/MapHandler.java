package com.senninha.tankc.map.handler;

import cn.senninha.sserver.lang.dispatch.MessageHandler;
import cn.senninha.sserver.lang.dispatch.MessageInvoke;
import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.message.CmdConstant;

@MessageHandler
public class MapHandler {
	@MessageInvoke(cmd = CmdConstant.MAP_RESOURCE_RES)
	public void receiveMap(int sessionId, BaseMessage message) {
		System.out.println(message.getCmd());
	}
}
