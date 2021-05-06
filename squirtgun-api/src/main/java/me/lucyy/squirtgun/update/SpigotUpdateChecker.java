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

package me.lucyy.squirtgun.update;

import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Update checking mechanism for Spigot plugins.
 */
public class SpigotUpdateChecker extends UpdateChecker {
	/**
	 * Creates a new update checker, and schedule update checking every 3 hours.
	 *
	 * @param plugin             the plugin to check against
	 * @param pluginId           the numeric spigot resource id
	 * @param updateMessage      the message to show in console and to players with the listener permission on join
	 * @param listenerPermission if a player holds this permission and an update is available, they will be sent the update message in chat
	 */
	public SpigotUpdateChecker(JavaPlugin plugin, int pluginId, Component updateMessage, String listenerPermission) {
		super(plugin, "https://api.spigotmc.org/legacy/update.php?resource=" + pluginId,
			updateMessage, listenerPermission);
	}

	@Override
	protected boolean checkDataForUpdate(String input) {
		return getPlugin().getDescription().getVersion().equals(input);
	}
}

