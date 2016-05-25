package io.igx.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Vinicius Carvalho
 */
public class TrafficGeneratorClientHandler extends SimpleChannelInboundHandler<Object> {

	private ChannelHandlerContext ctx;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

	}


}
