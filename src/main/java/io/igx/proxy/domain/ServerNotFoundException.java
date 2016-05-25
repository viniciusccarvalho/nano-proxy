package io.igx.proxy.domain;

/**
 * @author Vinicius Carvalho
 */
public class ServerNotFoundException extends RuntimeException {
	public ServerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerNotFoundException(String message) {
		super(message);
	}
}
