package cn.senninha.sserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.user.message.ReqHeartbeatMessge;
import com.senninha.tankc.user.message.ReqLoginMessage;

import cn.senninha.sserver.client.ClientSession;
import cn.senninha.sserver.lang.ByteBufUtil;
import cn.senninha.sserver.lang.codec.CodecFactory;
import cn.senninha.sserver.lang.dispatch.HandlerFactory;
import cn.senninha.sserver.lang.message.BaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

/**
 * 拆包并分发到对应的业务Handler
 * 
 * @author senninha on 2017年11月8日
 *
 */
public class DispatchHandler extends LengthFieldBasedFrameDecoder {
	private Logger logger = LoggerFactory.getLogger(DispatchHandler.class);

	public DispatchHandler(int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip, boolean failFast) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength,
				lengthAdjustment, initialBytesToStrip, failFast);
	}

	/**
	 * decode()--->channelRead()
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf msg)
			throws Exception {
		msg = (ByteBuf) super.decode(ctx, msg);
		if (msg == null) {
			return null;
		} else {
			if (msg != null) {
				BaseMessage message = CodecFactory.getInstance()
						.decode(ByteBufUtil.convert(msg));
				Integer sessionId = (Integer) (ctx.channel()
						.attr(AttributeKey.valueOf("sessionId"))).get();
				if (sessionId == null) {
					// 登陆
					sessionId = 12580;
					ctx.channel().attr(AttributeKey.valueOf("sessionId"))
							.set(sessionId);

				}
				HandlerFactory.getInstance().dispatch(message, sessionId);
			}
		}
		return null;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ReqLoginMessage m = new ReqLoginMessage();
		m.setSessionId(10086);
		m.setUsername("senninha");
		ctx.writeAndFlush(m);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
	        IdleStateEvent e = (IdleStateEvent) evt;
	        switch (e.state()) {
	            case READER_IDLE:
	                break;
	            case WRITER_IDLE:
	                break;
	            case ALL_IDLE:
	                handleAllIdle(ctx);
	                break;
	            default:
	                break;
	        }
	    }
	}
	
	private void handleAllIdle(ChannelHandlerContext ctx){
		ctx.writeAndFlush(new ReqHeartbeatMessge());
		logger.error("心跳信息");
	}
}
