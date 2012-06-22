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
