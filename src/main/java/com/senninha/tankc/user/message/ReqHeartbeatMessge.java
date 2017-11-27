package com.senninha.tankc.user.message;

import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.lang.message.Message;
import cn.senninha.sserver.message.CmdConstant;

@Message(cmd = CmdConstant.HEART_REQ)
public class ReqHeartbeatMessge extends BaseMessage {
	private long time;
	
	

	public ReqHeartbeatMessge() {
		this.time = System.currentTimeMillis();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "ReqHeartbeatMessge [time=" + time + "]";
	}

}
