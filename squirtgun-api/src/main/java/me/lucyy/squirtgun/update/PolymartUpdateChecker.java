package me.lucyy.squirtgun.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.lucyy.squirtgun.platform.Platform;
import net.kyori.adventure.text.Component;

/**
 * Update checking mechanism for plugins on Polymart.
 */
public class PolymartUpdateChecker extends UpdateChecker {
	private final JsonParser parser = new JsonParser();

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
		JsonObject object = parser.parse(input).getAsJsonObject();
		JsonObject response = object.getAsJsonObject("response");
		if (!response.get("success").getAsBoolean()) {
			getPlatform().getLogger().severe("Failed to access Polymart to check for updates! Please report this " +
				"to the plugin developer.");
			return false;
		}
		String version = response.getAsJsonObject("resource")
			.getAsJsonObject("updates")
			.getAsJsonObject("latest")
			.get("version").getAsString();

		return !getPlatform().getPluginVersion().equals(version);
	}
}
