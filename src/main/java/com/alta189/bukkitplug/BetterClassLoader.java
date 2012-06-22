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

import com.alta189.bukkitplug.util.ReflectionUtil;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.PluginClassLoader;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BetterClassLoader extends PluginClassLoader {
	private final Map<String, Class<?>> classCache = new HashMap<String, Class<?>>();
	private final JavaPluginLoader loader;
	private final BasePlugin plugin;

	public BetterClassLoader(PluginClassLoader loader, BasePlugin plugin) {
		this((JavaPluginLoader) ReflectionUtil.getFieldValue(loader, "loader"), loader.getURLs(), loader.getParent(), plugin);
	}

	public BetterClassLoader(JavaPluginLoader loader, URL[] urls, ClassLoader parent, BasePlugin plugin) {
		super(loader, urls, parent);
		this.loader = loader;
		this.plugin = plugin;
	}

	@Override
	protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
		Class<?> clazz = classCache.get(name);

		if (clazz != null) {
			return clazz;
		}

		try {
			clazz = super.findClass(name);
		} catch (Exception ignore) {
		}

		if (clazz == null && checkGlobal) {
			final Set<String> ignore = new HashSet<String>();

			clazz = searchDependencies(name, ignore);

			if (clazz == null) {
				clazz = searchSoftDependencies(name, ignore);

				if (clazz == null) {
					clazz = searchOtherPlugins(name, ignore);
				}
			}
		}

		if (clazz == null) {
			throw new ClassNotFoundException(name);
		}

		classCache.put(name, clazz);
		return clazz;
	}

	@Override
	public Set<String> getClasses() {
		return classCache.keySet();
	}

	@SuppressWarnings("unchecked")
	public Class<?> searchDependencies(String name, final Set<String> ignore) {
		if (plugin.getDescription().getDepend() != null && plugin.getDescription().getDepend().size() >= 1) {
			Map<String, PluginClassLoader> classLoaders = new CaseInsensitiveMap((Map<String, PluginClassLoader>) ReflectionUtil.getFieldValue(loader, "loaders"));
			for (String dependency : plugin.getDescription().getDepend()) {
				ignore.add(dependency.toLowerCase());
				PluginClassLoader classLoader = classLoaders.get(dependency);
				if (classLoader != null) {
					try {
						Class<?> clazz = ReflectionUtil.invokeMethod("findClass", classLoader, name, false);
						if (clazz != null) {
							return clazz;
						}
					} catch (Exception ignored) {
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Class<?> searchSoftDependencies(String name, final Set<String> ignore) {
		if (plugin.getDescription().getSoftDepend() != null && plugin.getDescription().getSoftDepend().size() >= 1) {
			Map<String, PluginClassLoader> classLoaders = new CaseInsensitiveMap((Map<String, PluginClassLoader>) ReflectionUtil.getFieldValue(loader, "loaders"));
			for (String dependency : plugin.getDescription().getSoftDepend()) {
				ignore.add(dependency.toLowerCase());
				PluginClassLoader classLoader = classLoaders.get(dependency);
				if (classLoader != null) {
					try {
						Class<?> clazz = ReflectionUtil.invokeMethod("findClass", classLoader, name, false);
						if (clazz != null) {
							return clazz;
						}
					} catch (Exception ignored) {
					}
				}
			}
		}
		return null;
	}

	public Class<?> searchOtherPlugins(String name, final Set<String> ignore) {
		Map<String, PluginClassLoader> classLoaders = new CaseInsensitiveMap((Map<String, PluginClassLoader>) ReflectionUtil.getFieldValue(loader, "loaders"));
		for (String plugin : classLoaders.keySet()) {
			if (ignore.contains(plugin.toLowerCase())) {
				continue;
			}
			PluginClassLoader classLoader = classLoaders.get(plugin);
			if (classLoader != null) {
				try {
					Class<?> clazz = ReflectionUtil.invokeMethod("findClass", classLoader, name, false);
					if (clazz != null) {
						return clazz;
					}
				} catch (Exception ignored) {
				}
			}
		}
		return null;
	}
}
