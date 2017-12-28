package com.senninha.tankc.map.message;

import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.lang.message.Message;
import cn.senninha.sserver.message.CmdConstant;

/**
 * 射中AI
 * @author senninha
 *
 */
@Message(cmd = CmdConstant.RES_SHOT_AI)
public class ResShotAiMessage extends BaseMessage {
	private int blood;
	private int sessionId;
	
	public static ResShotAiMessage valueOf(int blood, int sessionId) {
		ResShotAiMessage res = new ResShotAiMessage();
		res.setBlood(blood);
		res.setSessionId(sessionId);
		return res;
	}
	public int getBlood() {
		return blood;
	}
	public void setBlood(int blood) {
		this.blood = blood;
	}
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	
}
