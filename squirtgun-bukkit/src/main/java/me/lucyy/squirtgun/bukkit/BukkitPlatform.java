package me.lucyy.squirtgun.bukkit;

import com.google.common.annotations.Beta;
import me.lucyy.squirtgun.platform.AuthMode;
import me.lucyy.squirtgun.platform.EventListener;
import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.SquirtgunPlayer;
import me.lucyy.squirtgun.platform.scheduler.TaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * A Platform implementation for Bukkit.
 */
@Beta
public class BukkitPlatform implements Platform {

	private final JavaPlugin plugin;

	private final BukkitListenerAdapter listenerAdapter = new BukkitListenerAdapter();

	public BukkitPlatform(final JavaPlugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(listenerAdapter, plugin);
	}

	@Override
	public Logger getLogger() {
		return plugin.getLogger();
	}

	@Override
	public String getPluginName() {
		return plugin.getName();
	}

	@Override
	public String getPluginVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public AuthMode getAuthMode() {
		boolean onlineMode = Bukkit.getOnlineMode();
		try {
			if (Class.forName("org.spigotmc.SpigotConfig").getField("bungee").getBoolean(null)) {
				return AuthMode.BUNGEE;
			}
		} catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignored) {
			// probably running craftbukkit, we can safely ignore these
		}
		return onlineMode ? AuthMode.ONLINE : AuthMode.OFFLINE;
	}

	@Override
	public TaskScheduler getTaskScheduler() {
		return null;
	}

	@Override
	public void registerEventListener(EventListener listener) {
		listenerAdapter.addListener(listener);
	}

	@Override
	public void unregisterEventListener(EventListener listener) {
		listenerAdapter.removeListener(listener);
	}

	@Override
	public SquirtgunPlayer getPlayer(UUID uuid) {
		return new BukkitPlayer(Bukkit.getOfflinePlayer(uuid));
	}


	@Override
	@SuppressWarnings("deprecation") // blame the orange hash man. :(
	public SquirtgunPlayer getPlayer(String name) {
		return new BukkitPlayer(Bukkit.getOfflinePlayer(name));
	}
}
