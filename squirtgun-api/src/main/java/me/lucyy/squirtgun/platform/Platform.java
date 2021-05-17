package me.lucyy.squirtgun.platform;

import me.lucyy.squirtgun.platform.scheduler.TaskScheduler;
import java.util.List;
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

	TaskScheduler getTaskScheduler();

	void registerEventListener(EventListener listener);

	void unregisterEventListener(EventListener listener);

	SquirtgunPlayer getPlayer(UUID uuid);

	SquirtgunPlayer getPlayer(String name);

	List<SquirtgunPlayer> getOnlinePlayers();
}
