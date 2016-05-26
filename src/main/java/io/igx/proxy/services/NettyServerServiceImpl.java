package io.igx.proxy.services;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import io.igx.proxy.domain.ConnectionStats;
import io.igx.proxy.domain.CreateProxyRequest;
import io.igx.proxy.domain.ProxyDefinition;
import io.igx.proxy.domain.ServerNotFoundException;
import io.igx.proxy.domain.TrafficShaping;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import org.springframework.stereotype.Service;

/**
 * @author Vinicius Carvalho
 */
@Service
public class NettyServerServiceImpl implements NettyServerService {

	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;
	private Map<String, ProxyServer> servers;

	public NettyServerServiceImpl(){
		this.bossGroup = new NioEventLoopGroup(2);
		this.workerGroup = new NioEventLoopGroup(8);
		this.servers = new ConcurrentHashMap<>();
	}


	@Override
	public ProxyDefinition createServer(CreateProxyRequest createProxyRequest){

		ProxyDefinition proxyDefinition = new ProxyDefinition(createProxyRequest);
		if(!checkPortAvailable(proxyDefinition)){
			throw new IllegalStateException("Selected local port already in use");
		}
		servers.put(proxyDefinition.getId(),new TCPProxyServer(proxyDefinition));
		return proxyDefinition;
	}

	@Override
	public void startServer(String id) {
		findServer(id).start(bossGroup,workerGroup);
	}

	@Override
	public void stopServer(String id) {
		findServer(id).stop();
	}

	@Override
	public void configureTraffic(String id, TrafficShaping config) {
		ProxyServer server = findServer(id);
		server.getDefinition().getQos().setTrafficShaping(config);
		server.configureTraffic(config);
	}

	@Override
	public ProxyDefinition getProxyDefinition(String id) {
		return findServer(id).getDefinition();
	}


	@Override
	public Set<ProxyDefinition> listServers(){
		return servers.values().stream().map(p -> {return p.getDefinition();}).collect(Collectors.toSet());
	}

	@Override
	public ConnectionStats getStats(String id) {
		return findServer(id).getStats();
	}


	private ProxyServer findServer(String id){
		ProxyServer server = servers.get(id);
		if(server == null)
			throw new ServerNotFoundException("Server not found: " + id);
		return server;
	}

	@PreDestroy
	public void shutdown(){
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	/**
	 * Check if there's already a proxy defined for this port.
	 * @param proxyDefinition
	 */
	private boolean checkPortAvailable(ProxyDefinition proxyDefinition) {
		boolean portAvailble = true;

		for(Map.Entry<String,ProxyServer> entry : servers.entrySet()){
			if(entry.getValue().getDefinition().getLocalPort().equals(proxyDefinition.getLocalPort())){
				portAvailble = false;
				break;
			}
		}
		return portAvailble;
	}
}
