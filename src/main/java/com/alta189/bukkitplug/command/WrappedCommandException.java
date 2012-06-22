package com.alta189.bukkitplug.command;

public class WrappedCommandException extends CommandException {
	
	private static final long serialVersionUID = 9124773905653368232L;

	public WrappedCommandException(Throwable cause) {
		super(cause);
	}
}