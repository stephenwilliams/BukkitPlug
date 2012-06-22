package com.alta189.bukkitplug.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

	/**
	 * Short description of the command.
	 *
	 * @return description value
	 */
	String value();

}
