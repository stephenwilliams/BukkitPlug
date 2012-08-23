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
package com.alta189.bukkitplug;

import com.alta189.bukkitplug.command.CommandFactory;
import com.alta189.bukkitplug.command.CommandManager;
import com.alta189.bukkitplug.command.CommonCommand;
import com.alta189.commons.util.CastUtil;
import com.alta189.commons.util.ReflectionUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public abstract class BasePlugin extends JavaPlugin {
	private static final Yaml yaml = new Yaml();
	private CommandFactory factory = new CommandFactory(this);
	public boolean debugMode = false;

	@Override
	@SuppressWarnings("unchecked")
	public void onLoad() {
		JarFile file = null;
		InputStream inputStream = null;
		boolean classLoaderFix = true;
		try {
			file = new JarFile(getFile());

			JarEntry entry = file.getJarEntry("plugin.yml");

			if (entry == null) {
				throw new RuntimeException("Something has gone terribly wrong, the plugin.yml is missing after loading the plugin.");
			}

			inputStream = file.getInputStream(entry);

			Map<?, ?> map = (Map<?, ?>) yaml.load(inputStream);
			if (map.containsKey("debug")) {
				setDebugMode(map.get("debug").toString().equalsIgnoreCase("true"));
			}
			if (map.containsKey("classloader-fix")) {
				classLoaderFix = !map.get("debug").toString().equalsIgnoreCase("false");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException ignore) {
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ignored) {
				}
			}
		}

		if (classLoaderFix) {
			debug("Loaded in DEBUG MODE!");

			debug("Using reflection to replace ClassLoader");
			BetterClassLoader classLoader = new BetterClassLoader(CastUtil.safeCast(getClassLoader(), PluginClassLoader.class), this);
			ReflectionUtil.setFieldValue(this, "classLoader", this);

			if (getClassLoader() instanceof BetterClassLoader) {
				debug("Successfully replaced the classLoader in the plugin object");
			} else {
				debug("Unsuccessfully replaced the classLoader in the plugin object");
			}

			Map<String, PluginClassLoader> classLoaders = ReflectionUtil.getFieldValue(getPluginLoader(), "loaders");
			classLoaders.put(getName(), classLoader);

			if (((Map<String, PluginClassLoader>) ReflectionUtil.getFieldValue(getPluginLoader(), "loaders")).get(getName()) instanceof BetterClassLoader) {
				debug("Successfully replaced the classLoader in the plugin loader object");
			} else {
				debug("Unsuccessfully replaced the classLoader in the plugin loader object");
			}
		}
	}

	@Override
	public final void onEnable() {
		info("Enabling version: " + getDescription().getVersion());
		enable();
		info("Successfully enabled!");
	}

	public abstract void enable();

	@Override
	public final void onDisable() {
		info("Disabling version: " + getDescription().getVersion());
		disable();
		info("Successfully disabled!");
	}

	public abstract void disable();

	public void registerCommands(Class<?> clazz) {
		List<CommonCommand> commands = factory.build(clazz);
	    if (commands.size() > 0) {
			CommandManager.registerCommands(commands);
		}
	}

	public void log(Level level, String message) {
		getLogger().log(level, message);
	}

	public void log(Level level, String message, Throwable throwable) {
		getLogger().log(level, message, throwable);
	}

	public void debug(String message) {
		if (isDebugMode()) {
			log(Level.INFO, "[DEBUG] " + message);
		}
	}

	public void info(String message) {
		log(Level.INFO, message);
	}

	public void warning(String message) {
		log(Level.WARNING, message);
	}

	public void warning(String message, Throwable throwable) {
		log(Level.WARNING, message, throwable);
	}

	public void severe(String message) {
		log(Level.SEVERE, message);
	}

	public void severe(String message, Throwable throwable) {
		log(Level.SEVERE, message, throwable);
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
}
