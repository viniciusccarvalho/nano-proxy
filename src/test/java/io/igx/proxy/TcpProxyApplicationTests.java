package io.igx.proxy;

import java.util.HashMap;
import java.util.Map;

import io.igx.proxy.domain.CreateProxyRequest;
import io.igx.proxy.domain.ProxyDefinition;
import io.igx.proxy.domain.TrafficShapping;
import io.igx.proxy.services.NettyServerService;
import io.igx.proxy.services.NettyServerServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

public class TcpProxyApplicationTests {

	@Test
	public void testTrafficShapping() throws Exception{
		NettyServerService service = new NettyServerServiceImpl();
		ProxyDefinition proxyDefinition = new ProxyDefinition(new CreateProxyRequest());
		proxyDefinition.setLocalPort(16001);
		proxyDefinition.setRemoteHost("localhost");
		proxyDefinition.setRemotePort(5672);
		proxyDefinition.getQos().setTrafficShapping(new TrafficShapping(1_000l,1_000l));

	}

}
