package io.igx.proxy.handlers;

import io.igx.proxy.domain.ProxyDefinition;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;

/**
 * @author Vinicius Carvalho
 * Taken from https://github.com/netty/netty/blob/4.1/example/src/main/java/io/netty/example/proxy/HexDumpProxyFrontendHandler.java
 */
public class ProxyFrontendHandler extends ChannelInboundHandlerAdapter {

	private ProxyDefinition proxyDefinition;
	private volatile Channel outboundChannel;

	public ProxyFrontendHandler(ProxyDefinition proxyDefinition) {
		this.proxyDefinition = proxyDefinition;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		final Channel inboundChannel = ctx.channel();
		// Start the connection attempt.
		Bootstrap b = new Bootstrap();
		b.group(inboundChannel.eventLoop())
				.channel(ctx.channel().getClass())
				.handler(new ProxyBackendHandler(inboundChannel, proxyDefinition))
				.option(ChannelOption.AUTO_READ, false);
		ChannelFuture f = b.connect(proxyDefinition.getRemoteHost(), proxyDefinition.getRemotePort());
		outboundChannel = f.channel();
		f.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				if (future.isSuccess()) {
					// connection complete start to read first data
					inboundChannel.read();
				} else {
					// Close the connection if the connection attempt has failed.
					inboundChannel.close();
				}
			}
		});
	}
	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) {
		if (outboundChannel.isActive()) {
			if (ByteBuf.class.isAssignableFrom(msg.getClass())){
				proxyDefinition.getConnectionStats().appendBytesSent(((ByteBuf)msg).capacity());
			}
			outboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) {
					if (future.isSuccess()) {
						// was able to flush out data, start to read the next chunk
						ctx.channel().read();
					} else {
						future.channel().close();
					}
				}
			});
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		if (outboundChannel != null) {
			closeOnFlush(outboundChannel);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		closeOnFlush(ctx.channel());
	}

	/**
	 * Closes the specified channel after all queued write requests are flushed.
	 */
	static void closeOnFlush(Channel ch) {
		if (ch.isActive()) {
			ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}
}
