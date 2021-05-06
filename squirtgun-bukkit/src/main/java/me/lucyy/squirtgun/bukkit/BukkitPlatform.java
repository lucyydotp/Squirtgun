package me.lucyy.squirtgun.bukkit;

import me.lucyy.squirtgun.platform.AuthMode;
import me.lucyy.squirtgun.platform.Platform;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public abstract class BukkitPlatform implements Platform {

	private final JavaPlugin plugin;

	public BukkitPlatform(final JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public Logger getLogger() {
		return plugin.getLogger();
	}

	@Override
	public String getName() {
		return plugin.getName();
	}

	@Override
	public AuthMode getAuthMode() {
		boolean onlineMode = Bukkit.getOnlineMode();
		try {
			if (Class.forName("org.spigotmc.SpigotConfig").getField("bungee").getBoolean(null)) {
				return AuthMode.BUNGEE;
			}
		} catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignored) {
		}
		return onlineMode ? AuthMode.ONLINE : AuthMode.OFFLINE;
	}
}
