package com.alta189.bukkitplug.command;

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
