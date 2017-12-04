package com.senninha.tankc.map.message;

import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.lang.message.Message;
import cn.senninha.sserver.message.CmdConstant;

@Message(cmd = CmdConstant.RES_BATTLE_RESULT)
public class ResBattleResultMessage extends BaseMessage {
	private int fromSessionId;
	private int dieSessionId;
	private String name;
	@Override
	public String toString() {
		return "ReqBattleResultMessage [fromSessionId=" + fromSessionId + ", dieSessionId=" + dieSessionId + ", name="
				+ name + "]";
	}
	public int getFromSessionId() {
		return fromSessionId;
	}
	public void setFromSessionId(int fromSessionId) {
		this.fromSessionId = fromSessionId;
	}
	public int getDieSessionId() {
		return dieSessionId;
	}
	public void setDieSessionId(int dieSessionId) {
		this.dieSessionId = dieSessionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ResBattleResultMessage(int fromSessionId, int dieSessionId, String name) {
		super();
		this.fromSessionId = fromSessionId;
		this.dieSessionId = dieSessionId;
		this.name = name;
	}
	public ResBattleResultMessage() {
		super();
	}

}
