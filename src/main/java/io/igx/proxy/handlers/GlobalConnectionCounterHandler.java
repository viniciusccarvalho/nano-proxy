package io.igx.proxy.handlers;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Vinicius Carvalho
 */
public class GlobalConnectionCounterHandler extends ChannelInboundHandlerAdapter{

	private volatile Integer maxConnections = -1;
	private AtomicInteger counter;

	public GlobalConnectionCounterHandler(Integer maxConnections) {
		this.maxConnections = maxConnections;
		counter = new AtomicInteger(0);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if(maxConnections > -1){
			if(counter.get() < maxConnections)
				counter.incrementAndGet();
			else
				ctx.close();
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if(maxConnections > -1) {
			counter.decrementAndGet();
		}
	}
}
