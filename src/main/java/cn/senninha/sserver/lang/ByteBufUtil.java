package cn.senninha.sserver.lang;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 将
 * @author senninha on 2017年11月8日
 *
 */
public class ByteBufUtil {
	/**
	 *  {@link ByteBuf} 转化成 {@link ByteBuffer}
	 * @param buf
	 * @return
	 */
	public static ByteBuffer convert(ByteBuf buf){
		ByteBuffer buffer = ByteBuffer.allocate(buf.readableBytes());
		byte[] src = new byte[buf.readableBytes()];
		buf.getBytes(0, src);
		buffer.put(src);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * {@link ByteBuffer} 转化成 {@link ByteBuf}
	 * @param buf
	 * @return
	 */
	public static ByteBuf convert(ByteBuffer buffer){
		buffer.flip();
		int length = buffer.limit();
		byte[] src = new byte[3 + length];
		src[0] = 0x68;
		src[1] = (byte) ((length & 0xff_00) >> 8);
		src[2] = (byte) ((length & 0x00_ff));
		for(int i = 3; i < src.length; i++) {
			src[i] = buffer.get();
		}
		ByteBuf buf = Unpooled.copiedBuffer(src);
		return buf;
	}
}
