package com.senninha.tankc.map.message;

import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.lang.message.Message;
import cn.senninha.sserver.message.CmdConstant;

@Message(cmd = CmdConstant.RES_AI_DIE)
public class ResAiKillMessage extends BaseMessage {
	private int disSessionId;
	private String info;
	public int getDisSessionId() {
		return disSessionId;
	}
	public void setDisSessionId(int disSessionId) {
		this.disSessionId = disSessionId;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "ResAiKillMessage [disSessionId=" + disSessionId + ", info=" + info + "]";
	}
	
}
