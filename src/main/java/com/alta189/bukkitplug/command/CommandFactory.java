package com.alta189.bukkitplug.command;

import com.alta189.bukkitplug.BasePlugin;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandFactory {
	private final BasePlugin plugin;

	public CommandFactory(BasePlugin plugin) {
		this.plugin = plugin;
	}


	public List<CommonCommand> build(Class<?> clazz) {
		List<CommonCommand> result = new ArrayList<CommonCommand>();
		Class<?> search = clazz;
		while (clazz != null) {
			for (Method method : clazz.getDeclaredMethods()) {
				CommonCommand cmd = build(method);
				if (cmd != null) {
					result.add(cmd);
				}
			}
			search = search.getSuperclass();
		}
		return result;
	}

	public CommonCommand build(Method method) {
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

		if (parameters[0].equals(CommandSender.class) || parameters[1].equals(CommandContext.class)) {
			plugin.debug("Cannot register method because it's parameter types are incorrect: " + methodToString(method));
			return null;
		}

		Command command = method.getAnnotation(Command.class);
		Description description = method.getAnnotation(Description.class);

		List<String> aliases = new ArrayList<String>();
		if (method.isAnnotationPresent(Aliases.class)) {
			aliases.addAll(Arrays.asList(method.getAnnotation(Aliases.class).value()));
		}

		String usage = null;
		if (method.isAnnotationPresent(Usage.class)) {
			usage = method.getAnnotation(Usage.class).value();
		}
		CommonCommand cmd = new CommonCommand(command.name(), description.value(), usage, aliases, plugin);

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
		return cmd;
	}

	private String methodToString(Method method) {
		StringBuilder builder = new StringBuilder();
		builder.append("Cannot register '")
				.append(method.getDeclaringClass().getCanonicalName())
				.append(".")
				.append(method.getName())
				.append("(");

		TypeVariable[] params =  method.getTypeParameters();
		Class<?>[] types = method.getParameterTypes();
		if (params.length > 0) {
			int i = 0;
			for (TypeVariable param : params) {
				builder.append(types[0].getSimpleName())
						.append(" ")
						.append(param.getName());
				i ++;
			}
		}
		builder.append(")");
		return builder.toString();
	}

}
