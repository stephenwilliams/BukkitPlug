package com.alta189.bukkitplug.command;

import com.alta189.bukkitplug.util.ReflectionUtil;
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
