package cn.senninha.sserver.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.senninha.sserver.lang.message.BaseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ClientSession {
	private static Logger logger = LoggerFactory.getLogger(ClientSession.class);
	private ChannelHandlerContext ctx;
	private static ClientSession session;
	private long lastSendHeartbeat;  		//上一次发送心跳时间，一旦收到心跳回复，马上致0
	private int sessionId;

	public int getSessionId() {
		if(sessionId == 0){
			sessionId = (Integer) (ctx.channel()
					.attr(AttributeKey.valueOf("sessionId"))).get();
		}
		return sessionId;
	}
	
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
		if(ctx != null){
			ctx.channel().writeAndFlush(message);
		}else{
			logger.error("连接已经断开");
		}
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
