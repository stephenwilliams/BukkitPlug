package com.alta189.bukkitplug.command;

public class CommandException extends RuntimeException {
	private static final long serialVersionUID = 7936404856385100186L;

	public CommandException(String msg) {
		super(msg);
	}

	public CommandException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public CommandException(Throwable cause) {
		super(cause);
	}
}
