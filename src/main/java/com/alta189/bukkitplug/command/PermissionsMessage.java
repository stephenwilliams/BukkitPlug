package com.alta189.bukkitplug.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionsMessage {

	/**
	 * Allows you to override the "no permissions"
	 * message with a custom message
	 *
	 * @return message for no permission notification
	 */
	String value();

}
