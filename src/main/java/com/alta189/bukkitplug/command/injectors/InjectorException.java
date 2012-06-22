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
package com.alta189.bukkitplug.command.injectors;

public class InjectorException extends RuntimeException {
	public InjectorException(String message) {
		super(message);
	}

	public InjectorException(String message, Throwable cause) {
		super(message, cause);
	}

	public InjectorException(Throwable cause) {
		super(cause);
	}
}