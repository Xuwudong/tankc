package cn.senninha.sserver;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.senninha.sserver.handler.DispatchHandler;
import cn.senninha.sserver.handler.EncodeHandler;
import cn.senninha.sserver.lang.codec.CodecFactory;
import cn.senninha.sserver.lang.dispatch.HandlerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class ClientStart {
	public static ClientStart start;
	private Bootstrap bootstrap;
	private final String host;
	private final int port;
	private final int maxFrameLength; // 最大长度
	private final int lengthFieldOffset; // 偏移地址
	private final int lengthFieldLength; // 表示报文长度的字节数
	private final int lengthAdjustment; // 从何处开始计算包长度，默认是从报文长度的下一个字节开始
	private final int initialBytesToStrip; // 裁减包头
	private final boolean failFast; // 快速失败。一旦到达maxFrameLength，马上抛出异常。
	private Logger logger = LoggerFactory.getLogger(ClientStart.class);

	public ClientStart(String host, int port, int maxFrameLength,
			int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip, boolean failFast) {
		super();
		this.host = host;
		this.port = port;
		this.maxFrameLength = maxFrameLength;
		this.lengthFieldOffset = lengthFieldOffset;
		this.lengthFieldLength = lengthFieldLength;
		this.lengthAdjustment = lengthAdjustment;
		this.initialBytesToStrip = initialBytesToStrip;
		this.failFast = failFast;
	}

	public void connect() throws Exception {
		init(); // 初始化编解码工具
		EventLoopGroup workerGroup = new NioEventLoopGroup(1);

		try {
			bootstrap = new Bootstrap();
			bootstrap.group(workerGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new EncodeHandler());
					ch.pipeline().addLast(new IdleStateHandler(0, 0, 5));
					ch.pipeline().addLast(new DispatchHandler(maxFrameLength,
							lengthFieldOffset, lengthFieldLength,
							lengthAdjustment, initialBytesToStrip, failFast));
				}
			});

			// Start the client.

			doConnect();
			// Wait until the connection is closed.
		}catch (Exception e) {
			logger.error(e.toString());
		}
	}

	private void doConnect() {
		ChannelFuture future = null;
		future = bootstrap.connect(host, port);

		future.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture futureListener)
					throws Exception {
				if (futureListener.isSuccess()) {
					System.out.println("Connect to server successfully!");
				} else {
					System.out.println(
							"Failed to connect to server, try connect after 10s");

					futureListener.channel().eventLoop()
							.schedule(new Runnable() {
								@Override
								public void run() {
									doConnect();
								}
							}, 10, TimeUnit.SECONDS);
				}
			}
		});
	}

	private void init() {
		/** 初始化分发以及编解码工具 **/
		CodecFactory.getInstance();
		HandlerFactory.getInstance();
		logger.error("初始化编解码工具成功");
	}

	public static void main(String[] args) throws Exception {
		ClientStart client = new ClientStart("localhost", 9527, 1024 * 16, 1, 2, 0,
				3, true);
		start = client;
		client.connect();
	}
}