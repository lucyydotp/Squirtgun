package me.lucyy.common.format;

import me.lucyy.common.CommonLibVersion;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class Platform {
	private static Platform instance;
	public static Platform getInstance() {
		return instance;
	}

	private boolean isPaper;
	private BukkitAudiences audiences;

	public Platform(JavaPlugin plugin) throws ClassNotFoundException {
		try {
			Class.forName("net.kyori.adventure.text.Component");
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().severe("[" + plugin.getName() + "] * Adventure could not be found! *");
			Bukkit.getLogger().severe("Adventure is now a required dependency for " + plugin.getName() + ".");
			Bukkit.getLogger().severe("See the docs at https://docs.lucyy.me/adventure for more info.");
			Bukkit.getLogger().severe(plugin.getName() + " will now be disabled.");
			Bukkit.getLogger().severe("LucyCommonLib v" + CommonLibVersion.VERSION + ", server " + Bukkit.getVersion());
			throw e;
		}
		try {
			Class.forName("io.papermc.paper.chat.ChatComposer");
			isPaper = true;
		} catch (ClassNotFoundException e) {
			isPaper = false;
			audiences = BukkitAudiences.create(plugin);
		}
		instance = this;
	}

	private void sendInst(CommandSender target, Component message) {
		if (isPaper) target.sendMessage(message);
		else audiences.sender(target).sendMessage(message);
	}

	public static void send(CommandSender target, Component message) {
		getInstance().sendInst(target, message);
	}

	public static void close() {
		getInstance().audiences.close();
	}
}
