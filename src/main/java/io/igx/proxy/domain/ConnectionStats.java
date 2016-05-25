package io.igx.proxy.domain;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Vinicius Carvalho
 */
public class ConnectionStats {

	private AtomicInteger connectionCount = new AtomicInteger(0);
	private AtomicLong bytesSent = new AtomicLong(0L);
	private AtomicLong bytesReceived = new AtomicLong(0L);

	public ConnectionStats(){}
	public ConnectionStats(long bytesSent, long bytesReceived){
		this.bytesSent.set(bytesSent);
		this.bytesReceived.set(bytesReceived);
	}

	public int increaseConnectionCount(){
		return connectionCount.incrementAndGet();
	}

	public int decreaseConnectionCount(){
		return connectionCount.decrementAndGet();
	}

	public long appendBytesSent(long delta){
		return bytesSent.getAndAdd(delta);
	}

	public long appendBytesReceived(long delta){
		return bytesReceived.getAndAdd(delta);
	}



	public AtomicLong getBytesReceived() {
		return bytesReceived;
	}

	public Long getBytesSent(){
		return bytesSent.get();
	}

	public Integer getConnectionCount(){
		return connectionCount.get();
	}
}


