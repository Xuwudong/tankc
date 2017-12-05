package cn.senninha.sserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.user.message.ReqHeartbeatMessge;
import com.senninha.tankc.user.message.ReqLoginMessage;
import com.senninha.tankc.user.message.ResHeartbeatMessge;

import cn.senninha.sserver.client.ClientSession;
import cn.senninha.sserver.lang.ByteBufUtil;
import cn.senninha.sserver.lang.codec.CodecFactory;
import cn.senninha.sserver.lang.dispatch.HandleContext;
import cn.senninha.sserver.lang.message.BaseMessage;
import cn.senninha.sserver.message.CmdConstant;
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
	private static long timeWait = 1000 * 30;			//超时重连时间
	private int sessionId;

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
					sessionId = this.sessionId;
					ctx.channel().attr(AttributeKey.valueOf("sessionId"))
							.set(sessionId);

				}
				
				if(message.getCmd() == CmdConstant.HEART_RES){
					ResHeartbeatMessge res = (ResHeartbeatMessge) message;
					logger.error("ping：{}", (res.getCurrent() - res.getTime()));
					return null;
				}
				HandleContext.getInstance().dispatch(sessionId, message);
			}
		}
		return null;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ReqLoginMessage m = new ReqLoginMessage();
		sessionId = (int)(System.currentTimeMillis());
		m.setSessionId(sessionId);
		m.setUsername("senninha0-" + sessionId);
		ctx.writeAndFlush(m);
		ClientSession.getInstance().setCtx(ctx);
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
		ClientSession client = ClientSession.getInstance();
//		if(client.getLastSendHeartbeat() == 0) {
			ctx.writeAndFlush(new ReqHeartbeatMessge());
			client.setLastSendHeartbeat(System.currentTimeMillis());
//		}
		
		if(System.currentTimeMillis() - client.getLastSendHeartbeat() > timeWait) {
			ctx.disconnect();
			client.setCtx(null);
			try {
//				ClientStart.start.connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.error("掉线重连");
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ClientSession.getInstance().setCtx(null);
		ctx.disconnect();
//		ClientStart.start.doConnect();
	}
}
