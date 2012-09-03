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

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class CommonCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand {
	private final Plugin plugin;
	private String[] permissions = new String[0];
	private CommandExecutor executor;
	private int min = 0;
	private int max = -1;
	private String help;

	protected CommonCommand(String name, Plugin plugin) {
		super(name);
		this.plugin = plugin;
	}

	protected CommonCommand(String name, String description, String usage, List<String> aliases, Plugin plugin) {
		super(name, description, usage, aliases);
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String cmd, String[] args) {
		if (executor != null) {
			executor.onCommand(sender, this, cmd, args);
		}
		return true;
	}

	public String[] getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
		if (permissions != null) {
			super.setPermission(StringUtils.join(permissions, ";"));
		}
	}

	@Override
	public boolean testPermissionSilent(CommandSender target) {
		if (permissions == null || permissions.length == 0) {
			return true;
		}

		for (String perm : permissions) {
			if (target.hasPermission(perm)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Plugin getPlugin() {
		return plugin;
	}

	public CommandExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(CommandExecutor executor) {
		this.executor = executor;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
}
