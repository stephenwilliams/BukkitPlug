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

import com.alta189.commons.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.util.List;

public class CommandManager {

	public static CommandMap getCommandMap() {
		CommandMap commandMap = ReflectionUtil.getFieldValue(SimplePluginManager.class, Bukkit.getServer().getPluginManager(), "commandMap");
		if (commandMap != null) {
			return commandMap;
		}
		return null;
	}

	public static void registerCommand(CommonCommand command) {
		getCommandMap().register(command.getPlugin().getName(), command);
	}

	public static void registerCommands(List<CommonCommand> commands) {
		CommandMap commandMap = getCommandMap();
		for (CommonCommand command : commands) {
			commandMap.register(command.getPlugin().getName(), command);
		}
	}

}
