package io.igx.proxy.domain;

/**
 * @author Vinicius Carvalho
 */
public class Qos {

	private TrafficShaping trafficShaping;

	public TrafficShaping getTrafficShaping() {
		return trafficShaping;
	}

	public void setTrafficShaping(TrafficShaping trafficShapping) {
		this.trafficShaping = trafficShapping;
	}
}
