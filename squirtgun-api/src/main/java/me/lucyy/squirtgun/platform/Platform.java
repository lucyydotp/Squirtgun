/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lucyy.squirtgun.platform;

import me.lucyy.squirtgun.platform.scheduler.TaskScheduler;
import me.lucyy.squirtgun.plugin.SquirtgunPlugin;
import net.kyori.adventure.text.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * A wrapper around a server platform's API, specific to a plugin.
 * This is a work-in-progress API and more functionality is planned.
 *
 * @since 2.0.0
 */
public interface Platform {
	/**
	 * Gets the plugin's logger for logging text to the console.
	 */
	Logger getLogger();

	/**
	 * Logs a formatted component to the console.
	 * @param component the component to log
	 */
	void log(Component component);

	/**
	 * Gets the server's authentication mode.
	 */
	AuthMode getAuthMode();

	/**
	 * Gets the server's task scheduler.
	 */
	TaskScheduler getTaskScheduler();

	/**
	 * Registers an event listener.
	 * @param listener the listener to register
	 */
	void registerEventListener(EventListener listener);

	/**
	 * Unregisters an event listener. If the listener is not registered, this does nothing.
	 * @param listener the listener to unregister.
	 */
	void unregisterEventListener(EventListener listener);

	/**
	 * Gets a player by UUID.
	 * @param uuid the UUID of the player to get
	 * @return the player
	 */
	SquirtgunPlayer getPlayer(UUID uuid);

	/**
	 * Gets a player by name.
	 * @param name the name of the player to get
	 * @return the player with the given name.
	 */
	SquirtgunPlayer getPlayer(String name);

	/**
	 * Gets a list of all online players.
	 */
	List<SquirtgunPlayer> getOnlinePlayers();

	/**
	 * Gets a path to a directory where config files specific to a plugin can be stored.
	 */
	Path getConfigPath(SquirtgunPlugin<?> plugin);
}
