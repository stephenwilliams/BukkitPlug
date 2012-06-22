package com.alta189.bukkitplug.command;

import com.alta189.bukkitplug.CastUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommonCommandExecutor implements CommandExecutor {
	private final Object target;
	private final Method method;

	public CommonCommandExecutor(Object target, Method method) {
		this.target = target;
		this.method = method;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		CommonCommand commonCommand = CastUtil.safeCast(command);
		CommandContext commandContext = new CommandContext(strings);
		if (commandContext.length() >= commonCommand.getMin() && (commonCommand.getMax() == -1 || commonCommand.getMax() <= commandContext.length())) {
			try {
				List<Object> commandArgs = new ArrayList<Object>();
				commandArgs.add(commandContext);
				commandArgs.add(commandSender);
				method.invoke(target, commandArgs.toArray());
			} catch (IllegalAccessException e) {
				throw new WrappedCommandException(e);
			} catch (InvocationTargetException e) {
				if (e.getCause() == null) {
					throw new WrappedCommandException(e);
				}

				Throwable cause = e.getCause();
				if (cause instanceof CommandException) {
					throw (CommandException) cause;
				}

				throw new WrappedCommandException(cause);
			}
		} else {
			if (command.getUsage() != null) {
				commandSender.sendMessage("Correct usage: " + commonCommand.getUsage());
			} else {
				commandSender.sendMessage("Incorrect usage.");
			}
		}

		return true;
	}
}
