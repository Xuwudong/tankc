package cn.senninha.sserver.client;

import cn.senninha.sserver.lang.message.BaseMessage;
import io.netty.channel.ChannelHandlerContext;

public class ClientSession {
	private ChannelHandlerContext ctx;
	private static ClientSession session;

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

	@Override
	public String toString() {
		return "ClientSession [ctx=" + ctx + "]";
	}

}
