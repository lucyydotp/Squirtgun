package me.lucyy.squirtgun.platform;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * TODO javadocs
 */
public interface Platform {

	Logger getLogger();

	String getPluginName();

	String getPluginVersion();

	AuthMode getAuthMode();

	void registerEventListener(EventListener listener);

	void unregisterEventListener(EventListener listener);

	Player getPlayer(UUID uuid);

	Player getPlayer(String name);
}
