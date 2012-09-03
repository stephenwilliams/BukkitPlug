/*
 * This file is part of BukkitPlug.
 *
 * Copyright (c) 2012, alta189 <http://alta189.com/>
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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alta189.bukkitplug.BasePlugin;
import com.alta189.commons.util.injectors.Injector;

import org.bukkit.command.CommandSender;

public class CommandFactory {
	private final BasePlugin plugin;

	public CommandFactory(BasePlugin plugin) {
		this.plugin = plugin;
	}

	public List<CommonCommand> build(Class<?> clazz, Injector injector) {
		Object o = injector.newInstance(clazz);
		List<CommonCommand> result = new ArrayList<CommonCommand>();
		Class<?> search = clazz;
		while (search != null) {
			for (Method method : search.getDeclaredMethods()) {
				CommonCommand cmd = build(method, o);
				if (cmd != null) {
					result.add(cmd);
				}
			}
			search = search.getSuperclass();
		}
		return result;
	}

	public CommonCommand build(Method method, Object o) {
		method.setAccessible(true);

		// @Command and @Description are required
		if (!method.isAnnotationPresent(Command.class) || !method.isAnnotationPresent(Description.class)) {
			return null;
		}

		if (Modifier.isStatic(method.getModifiers())) {
			plugin.debug("Cannot register static method: " + methodToString(method));
			return null;
		}

		Class<?>[] parameters = method.getParameterTypes();
		if (parameters.length != 2) {
			plugin.debug("Cannot register method because it's parameter types are incorrect: " + methodToString(method));
			return null;
		}

		if (!parameters[0].equals(CommandSender.class) || !parameters[1].equals(CommandContext.class)) {
			plugin.debug("Cannot register method because it's parameter types are incorrect: " + methodToString(method));
			return null;
		}

		Command command = method.getAnnotation(Command.class);
		String name = command.name();
		if (name == null || name.isEmpty()) {
			name = method.getName();
		}

		Description description = method.getAnnotation(Description.class);

		List<String> aliases = new ArrayList<String>();
		if (method.isAnnotationPresent(Aliases.class)) {
			aliases.addAll(Arrays.asList(method.getAnnotation(Aliases.class).value()));
		}

		String usage = "";
		if (method.isAnnotationPresent(Usage.class)) {
			usage = method.getAnnotation(Usage.class).value();
		}
		CommonCommand cmd = new CommonCommand(name, description.value(), usage, aliases, plugin);

		if (command.max() > -1) {
			cmd.setMax(command.max());
		}

		if (command.min() > 0 && (cmd.getMax() == -1 || cmd.getMax() >= command.min())) {
			cmd.setMin(command.min());
		}

		if (method.isAnnotationPresent(Help.class)) {
			cmd.setHelp(method.getAnnotation(Help.class).value());
		}

		if (method.isAnnotationPresent(Permissions.class)) {
			cmd.setPermissions(method.getAnnotation(Permissions.class).value());
		}

		if (method.isAnnotationPresent(PermissionsMessage.class)) {
			cmd.setPermissionMessage(method.getAnnotation(PermissionsMessage.class).value());
		}

		CommonCommandExecutor executor = new CommonCommandExecutor(o, method);
		cmd.setExecutor(executor);
		return cmd;
	}

	private String methodToString(Method method) {
		StringBuilder builder = new StringBuilder();
		builder.append("Cannot register '")
				.append(method.getDeclaringClass().getCanonicalName())
				.append(".")
				.append(method.getName())
				.append("(");

		TypeVariable[] params = method.getTypeParameters();
		Class<?>[] types = method.getParameterTypes();
		if (params.length > 0) {
			int i = 0;
			for (TypeVariable param : params) {
				builder.append(types[0].getSimpleName())
						.append(" ")
						.append(param.getName());
				i++;
			}
		}
		builder.append(")");
		return builder.toString();
	}
}
