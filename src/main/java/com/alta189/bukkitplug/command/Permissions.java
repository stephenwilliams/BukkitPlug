package com.alta189.bukkitplug.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {

	/**
	 * Array of permissions. To use the command,
	 * only one permission needs to match.
	 *
	 * @return array of permissions
	 */
	String[] value();

}
