package com.senninha.tankc.user.message;

import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.lang.message.Message;
import cn.senninha.sserver.message.CmdConstant;

@Message(cmd = CmdConstant.RES_MATCH)
public class ResMatchMessage extends BaseMessage{
	private String pmt;
	
	/**
	 * 
	 * @param pmt 匹配提示信息
	 * @return
	 */
	public static ResMatchMessage valueOf(String pmt) {
		ResMatchMessage res = new ResMatchMessage();
		res.setPmt(pmt);
		return res;
	}

	public String getPmt() {
		return pmt;
	}

	public void setPmt(String pmt) {
		this.pmt = pmt;
	}

	@Override
	public String toString() {
		return "ResMatchMessage [pmt=" + pmt + "]";
	}
	
}
