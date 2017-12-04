package com.senninha.tankc.user.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.ui.GameData;
import com.senninha.tankc.user.message.ReqMatchMessage;
import com.senninha.tankc.user.message.ResLoginMessage;

import cn.senninha.sserver.client.ClientSession;
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
			
			//开始匹配
			ClientSession.getInstance().pushMessage(new ReqMatchMessage());
			
			logger.error("请求匹配中");
			GameData.getInstance().updateInfo("正在匹配中.......");
		}else {
			logger.error(m.getInfo());
		}
	}
	
	@MessageInvoke(cmd = CmdConstant.HEART_RES)
	public void heartbeat(int sessionId, BaseMessage message) {
		logger.error("收到心跳回复：{}", message.toString());
		ClientSession.getInstance().setLastSendHeartbeat(0);
	}
	
	@MessageInvoke(cmd = CmdConstant.RES_MATCH)
	public void resMatch(int sessionId, BaseMessage message) {
		logger.error("等待匹配：{}", message.toString());
	}
}
