package io.igx.proxy;

import io.igx.proxy.domain.CreateProxyRequest;
import io.igx.proxy.domain.ProxyDefinition;
import io.igx.proxy.domain.TrafficShaping;
import io.igx.proxy.services.NettyServerService;
import io.igx.proxy.services.NettyServerServiceImpl;
import org.junit.Test;

public class TcpProxyApplicationTests {

	@Test
	public void testTrafficShapping() throws Exception{
		NettyServerService service = new NettyServerServiceImpl();
		ProxyDefinition proxyDefinition = new ProxyDefinition(new CreateProxyRequest());
		proxyDefinition.setLocalPort(16001);
		proxyDefinition.setRemoteHost("localhost");
		proxyDefinition.setRemotePort(5672);
		proxyDefinition.getQos().setTrafficShaping(new TrafficShaping(1_000l,1_000l));

	}

}
