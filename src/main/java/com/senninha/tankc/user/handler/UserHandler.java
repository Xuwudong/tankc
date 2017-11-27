package com.senninha.tankc.user.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.senninha.tankc.user.message.ResLoginMessage;

import cn.senninha.sserver.lang.dispatch.MessageHandler;
import cn.senninha.sserver.lang.dispatch.MessageInvoke;
import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.message.CmdConstant;

@MessageHandler
public class UserHandler {
	private Logger logger = LoggerFactory.getLogger(UserHandler.class);
	
	@MessageInvoke(cmd = CmdConstant.LOGIN_RES)
	public void login(int sessionId, BaseMessage message) {
		ResLoginMessage m = (ResLoginMessage) message;
		if(m.getStatus() == 1) {
			logger.error("登陆成功");
			
		}else {
			logger.error(m.getInfo());
		}
	}
	
	@MessageInvoke(cmd = CmdConstant.HEART_RES)
	public void heartbeat(int sessionId, BaseMessage message) {
		logger.error("收到心跳回复：{}", message.toString());
	}
}
