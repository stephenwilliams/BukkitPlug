package com.alta189.bukkitplug.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aliases {

	/**
	 * Array of aliases for given command.
	 *
	 * @return aliases value
	 */
	String[] value();

}
