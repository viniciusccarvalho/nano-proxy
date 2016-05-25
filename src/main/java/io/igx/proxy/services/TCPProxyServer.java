package io.igx.proxy.services;

import io.igx.proxy.domain.ProxyDefinition;
import io.igx.proxy.handlers.ProxyFrontendHandler;
import io.igx.proxy.handlers.ProxyInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Vinicius Carvalho
 */
public class TCPProxyServer extends AbstractProxyServer {

	public TCPProxyServer(ProxyDefinition definition) {
		super(definition);
	}

	@Override
	protected Channel doStart(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		final ProxyFrontendHandler frontendHandler = new ProxyFrontendHandler(definition);
		ChannelFuture cf = bootstrap.group(bossGroup,workerGroup)
				.option(ChannelOption.SO_REUSEADDR,false)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ProxyInitializer(definition,trafficHandler))
				.childOption(ChannelOption.AUTO_READ,false)
				.bind(definition.getLocalPort());

		return cf.channel();

	}
}
