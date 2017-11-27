package cn.senninha.sserver.client;

import cn.senninha.sserver.lang.message.BaseMessage;
import io.netty.channel.ChannelHandlerContext;

public class ClientSession {
	private ChannelHandlerContext ctx;
	private static ClientSession session;
	private long lastSendHeartbeat;  		//上一次发送心跳时间，一旦收到心跳回复，马上致0

	private ClientSession() {

	}

	public static ClientSession getInstance() {
		if (session == null) {
			synchronized (ClientSession.class) {
				if (session == null) {
					session = new ClientSession();
				}
			}
		}
		return session;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public void pushMessage(BaseMessage message) {
		ctx.channel().writeAndFlush(message);
	}
	
	public long getLastSendHeartbeat() {
		return lastSendHeartbeat;
	}

	public void setLastSendHeartbeat(long lastSendHeartbeat) {
		this.lastSendHeartbeat = lastSendHeartbeat;
	}

	@Override
	public String toString() {
		return "ClientSession [ctx=" + ctx + "]";
	}

}
