package com.alta189.bukkitplug.command.injectors;

public interface Injector {
	public Object newInstance(Class<?> clazz);
}