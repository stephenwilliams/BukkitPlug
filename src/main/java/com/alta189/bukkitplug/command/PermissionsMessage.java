package com.alta189.bukkitplug.command;

public @interface PermissionsMessage {

	/**
	 * Allows you to override the "no permissions"
	 * message with a custom message
	 *
	 * @return message for no permission notification
	 */
	String value();

}
