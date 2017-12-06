package cn.senninha.sserver;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.senninha.tankc.map.Grid;
import com.senninha.tankc.map.GridStatus;
import com.senninha.tankc.map.MapHelper;
import com.senninha.tankc.map.util.ASNode;
import com.senninha.tankc.map.util.ASUtil;
import com.senninha.tankc.ui.GameData;
import com.senninha.tankc.ui.GameFrame;
import com.senninha.tankc.ui.util.DrawUtil;

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

	public void doConnect() {
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
//
//					futureListener.channel().eventLoop()
//							.schedule(new Runnable() {
//								@Override
//								public void run() {
//									doConnect();
//								}
//							}, 10, TimeUnit.SECONDS);
				}
			}
		});
	}

	public void init() {
		/** 初始化分发以及编解码工具 **/
		CodecFactory.getInstance();
		HandlerFactory.getInstance();
		logger.error("初始化编解码工具成功");
	}
	
	private void startUI() {
		GameFrame gameFrame = new GameFrame();
		gameFrame.setSize((int)Math.round((DrawUtil.WIDTH_GRIDS * 1.2)) * DrawUtil.PER_PXIEL, (DrawUtil.HEIGHT_GRIDS + 10)* DrawUtil.PER_PXIEL);
		gameFrame.setTitle("senninha_tank");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);
		//设置gameFram进游戏画面
		GameData.getInstance().setGameFrame(gameFrame);

		// 显示器屏幕大小
		Dimension screenSizeInfo = Toolkit.getDefaultToolkit().getScreenSize();
		int leftTopX = ((int) screenSizeInfo.getWidth() - gameFrame.getWidth()) / 2;
		int leftTopY = ((int) screenSizeInfo.getHeight() - gameFrame
				.getHeight()) / 2;

		// 设置显示的位置在屏幕中间
		gameFrame.setLocation(leftTopX, leftTopY);
	}

	public static void main(String[] args) throws Exception {
		String host = "localhost";
		if(args != null && args.length != 0){
			host = args[0];
		}
		ClientStart client = new ClientStart(host, 9527, 1024 * 16, 1, 2, 0,
				3, true);
		start = client;
		client.connect();
		client.startUI();
		/** 寻路测试
		List<Grid> grids = MapHelper.generateGridRandom();
		Grid g = grids.get(220);
		g.setStatus((byte)GridStatus.CAN_RUN.getStatus());
		ASNode node = ASUtil.aStar(grids, grids.get(0), grids.get(94), 20, 15);
		GameData.getInstance().setMap(grids);
		GameData.getInstance().setInGame(true);

		System.out.println(node.toString());
		
		GameData.getInstance().updateMap();
		 **/
	}
}