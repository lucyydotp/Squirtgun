/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of LucyCommonLib.
 *
 * LucyCommonLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LucyCommonLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LucyCommonLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucyy.common.update;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Update checking mechanism for Spigot plugins.
 */
public class UpdateChecker {
	private boolean updateAvailable;
	private final JavaPlugin plugin;
	private final String updateMessage;
	private final String listenerPermission;
	private final String url;
	private final BukkitTask listenerTask;

	/**
	 * Creates a new update checker, and schedule update checking every 3 hours.
	 *
	 * @param plugin             the plugin to check against
	 * @param url                a URL (such as the spigot api) that will return a plaintext version string when retrieved
	 * @param updateMessage      the message to show in console and to players with the listener permission on join
	 * @param listenerPermission if a player holds this permission and an update is available, they will be sent the update message in chat
	 */
	public UpdateChecker(JavaPlugin plugin, String url, String updateMessage, String listenerPermission) {
		this.url = url;
		this.plugin = plugin;
		this.updateMessage = updateMessage;
		this.listenerPermission = listenerPermission;
		Bukkit.getPluginManager().registerEvents(new UpdateListener(this), plugin);

		if (plugin.getDescription().getVersion().contains("-")) {
			plugin.getLogger().warning("Development version detected, skipping update check.");
			listenerTask = null;
			return;
		}

		listenerTask = new BukkitRunnable() {
			@Override
			public void run() {
				checkForUpdate();
			}
		}.runTaskTimerAsynchronously(plugin, 0, 216000); // every 3 hours
	}

	@SuppressWarnings("UnusedReturnValue")
	public boolean checkForUpdate() {
		try {
			plugin.getLogger().info("Checking for updates...");

			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			if (con.getResponseCode() != 200) throw new Exception();

			String text = new BufferedReader(
					new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)
			).lines().collect(Collectors.joining("\n"));

			if (!text.equals(plugin.getDescription().getVersion())) {
				updateAvailable = true;
				plugin.getLogger().warning(getUpdateMessage());
				listenerTask.cancel();
				return true;
			} else plugin.getLogger().info("No update available.");

		} catch (Exception ignored) {
			plugin.getLogger().warning("Unable to check for updates!");
		}
		return false;
	}

	/**
	 * @return the update message as set in the constructor
	 */
	public String getUpdateMessage() {
		return updateMessage;
	}

	/**
	 * @return whether there is an update available. Note that this will not check for updates, only return a cached result
	 */
	public boolean isUpdateAvailable() {
		return updateAvailable;
	}

	public String getListenerPermission() {
		return listenerPermission;
	}
}