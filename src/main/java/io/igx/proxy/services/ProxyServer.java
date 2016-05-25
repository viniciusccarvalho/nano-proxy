package io.igx.proxy.services;

import io.igx.proxy.domain.ConnectionStats;
import io.igx.proxy.domain.ProxyDefinition;
import io.netty.channel.EventLoopGroup;

/**
 * @author Vinicius Carvalho
 */
public interface ProxyServer {
	ProxyDefinition getDefinition();
	ConnectionStats getStats();
	void start(EventLoopGroup bossGroup, EventLoopGroup workerGroup);
	void stop();
}
