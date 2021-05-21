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

package me.lucyy.squirtgun.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.lucyy.squirtgun.platform.Platform;
import net.kyori.adventure.text.Component;

/**
 * Update checking mechanism for plugins on Polymart.
 */
public class PolymartUpdateChecker extends UpdateChecker {

	/**
	 * Creates a new update checker, and schedule update checking every 3 hours.
	 *
	 * @param plugin             the plugin to check against
	 * @param id                 the numeric polymart resource id
	 * @param updateMessage      the message to show in console and to players with the listener permission on join
	 * @param listenerPermission if a player holds this permission and an update is available, they will be sent the
	 *                           update message in chat
	 */
	public PolymartUpdateChecker(Platform plugin, int id, Component updateMessage, String listenerPermission) {
		super(plugin, "https://api.polymart.org/v1/getResourceInfo?resource_id=" + id,
			updateMessage, listenerPermission);
	}

	@Override
	protected boolean checkDataForUpdate(String input) {
		JsonObject object = JsonParser.parseString(input).getAsJsonObject();
		JsonObject response = object.getAsJsonObject("response");
		if (!response.get("success").getAsBoolean()) {
			getPlatform().getLogger().severe(
					"Failed to access Polymart to check for updates! Please report this to the plugin developer.");
			return false;
		}
		String version = response.getAsJsonObject("resource")
			.getAsJsonObject("updates")
			.getAsJsonObject("latest")
			.get("version").getAsString();

		return !getPlatform().getPluginVersion().equals(version);
	}
}
