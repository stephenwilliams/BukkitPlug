/*
 * This file is part of BukkitPlug.
 *
 * Copyright (c) 2012, alta189 <http://alta189.com//>
 * BukkitPlug is licensed under the GPL.
 *
 * BukkitPlug is free software: you can redistribute it and/or modify
 * it under the terms of the GPL as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BukkitPlug is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GPL for more details.
 *
 * You should have received a copy of the GPL
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alta189.bukkitplug.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * The name of the command. If the string is empty,
	 * it will default to the method name.
	 *
	 * @return name of the command
	 */
	String name() default "";

	/**
	 * The minimum number of arguments. Needs to
	 * greater than or equal to 0;
	 *
	 * When minimum is not met, the correct usage is
	 * shown.
	 *
	 * @return min number of arguments
	 */
	int min() default 0;

	/**
	 * The maximum number of arguments Needs to
	 * be greater than or equal to -1. For unlimited
	 * arguments use -1.
	 *
	 * When maximum is not met, the correct usage is
	 * shown.
	 *
	 * @return max number of arguments
	 */
	int max() default -1;
}
