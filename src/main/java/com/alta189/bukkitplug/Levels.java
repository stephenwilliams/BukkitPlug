package com.alta189.bukkitplug;

import java.util.logging.Level;

public class Levels extends Level {
	public static final Level DEBUG = new Levels("DEBUG", INFO.intValue() - 1);

	protected Levels(String name, int value) {
		super(name, value);
	}

	protected Levels(String name, int value, String resourceBundleName) {
		super(name, value, resourceBundleName);
	}
}
