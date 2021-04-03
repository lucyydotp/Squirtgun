package me.lucyy.common;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DependencyChecker {
    public static boolean adventurePresent(JavaPlugin plugin) {
        try {
            Class.forName("io.papermc.paper.chat.ChatComposer");
            return true;
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().severe("[" + plugin.getName() + "] * Adventure could not be found! *");
            Bukkit.getLogger().severe("Adventure is now a required dependency for " + plugin.getName() + ".");
            Bukkit.getLogger().severe("Usually this means that you just need to install/update Paper or a fork.");
            Bukkit.getLogger().severe("See the docs at https://docs.lucyy.me/adventure for more info.");
            Bukkit.getLogger().severe(plugin.getName() + " will now be disabled.");
            Bukkit.getLogger().severe("LucyCommonLib v" + CommonLibVersion.VERSION + ", server " + Bukkit.getVersion());
        }
        return false;
    }
}
