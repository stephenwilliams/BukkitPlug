package com.alta189.bukkitplug.command.injectors;

public class InjectorException extends RuntimeException {
	public InjectorException(String message) {
		super(message);
	}

	public InjectorException(String message, Throwable cause) {
		super(message, cause);
	}

	public InjectorException(Throwable cause) {
		super(cause);
	}
}