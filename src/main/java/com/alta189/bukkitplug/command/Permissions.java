package com.alta189.bukkitplug.command;

public @interface Permissions {

	/**
	 * Array of permissions. To use the command,
	 * only one permission needs to match.
	 *
	 * @return array of permissions
	 */
	String[] value();

}
