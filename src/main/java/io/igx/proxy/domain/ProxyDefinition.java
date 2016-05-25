package io.igx.proxy.domain;

import java.util.UUID;

/**
 * @author Vinicius Carvalho
 */
public class ProxyDefinition {
	private String alias;
	private final long startedTime;
	private final String id;
	private Integer remotePort;
	private String remoteHost;
	private boolean debug;
	private Integer localPort;
	private boolean active = false;
	private ConnectionStats connectionStats;
	private Qos qos;

	public ProxyDefinition(CreateProxyRequest request){

		this.id = UUID.randomUUID().toString();
		this.startedTime = System.currentTimeMillis();
		this.connectionStats =  new ConnectionStats();
		this.qos = new Qos();
		this.remoteHost = request.getRemoteHost();
		this.remotePort = request.getRemotePort();
		this.localPort = request.getLocalPort();
		this.alias = request.getAlias();

	}



	public Integer getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(Integer remotePort) {
		this.remotePort = remotePort;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Integer getLocalPort() {
		return localPort;
	}

	public void setLocalPort(Integer localPort) {
		this.localPort = localPort;
	}

	public String getId() {
		return id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public ConnectionStats getConnectionStats() {
		return connectionStats;
	}

	public void setConnectionStats(ConnectionStats connectionStats) {
		this.connectionStats = connectionStats;
	}

	public long getStartedTime() {
		return startedTime;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Qos getQos() {
		return qos;
	}

	public void setQos(Qos qos) {
		this.qos = qos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProxyDefinition that = (ProxyDefinition) o;

		return id.equals(that.id);

	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
