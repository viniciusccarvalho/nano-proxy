package io.igx.proxy.handlers;

import io.igx.proxy.domain.ProxyDefinition;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Vinicius Carvalho
 * Taken from: https://github.com/netty/netty/blob/4.1/example/src/main/java/io/netty/example/proxy/HexDumpProxyBackendHandler.java
 */
public class ProxyBackendHandler extends ChannelInboundHandlerAdapter {

	private final Channel inboundChannel;
	private final ProxyDefinition proxyDefinition;

	public ProxyBackendHandler(Channel inboundChannel, ProxyDefinition proxyDefinition) {
		this.inboundChannel = inboundChannel;
		this.proxyDefinition = proxyDefinition;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.read();
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) {
		if (ByteBuf.class.isAssignableFrom(msg.getClass())){
			proxyDefinition.getConnectionStats().appendBytesReceived(((ByteBuf)msg).capacity());
		}

		inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				if (future.isSuccess()) {
					ctx.channel().read();
				} else {
					future.channel().close();
				}
			}
		});
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		ProxyFrontendHandler.closeOnFlush(inboundChannel);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ProxyFrontendHandler.closeOnFlush(ctx.channel());
	}
}
