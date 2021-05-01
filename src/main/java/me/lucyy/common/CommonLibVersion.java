package me.lucyy.common;

import org.bukkit.Bukkit;

// intellij complains about this class a lot
public class CommonLibVersion {
	public static final String VERSION = "@VERSION@" + getAuthMode();

	private static String getAuthMode() {
		boolean onlineMode = Bukkit.getOnlineMode();
		try {
			if (Class.forName("org.spigotmc.SpigotConfig").getField("bungee").getBoolean(null)) {
				return "-b";
			}
		} catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignored) {
		}
		return onlineMode ? "" : "-c";
	}
}
