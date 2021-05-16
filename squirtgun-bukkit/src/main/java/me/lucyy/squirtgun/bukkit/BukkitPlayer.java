package me.lucyy.squirtgun.bukkit;

import me.lucyy.squirtgun.platform.Gamemode;
import me.lucyy.squirtgun.platform.SquirtgunPlayer;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BukkitPlayer implements SquirtgunPlayer {

    private final OfflinePlayer parent;
    private static final EnumMap<org.bukkit.GameMode, Gamemode> gamemodeMap = new EnumMap<>(org.bukkit.GameMode.class);

    static {
        gamemodeMap.put(GameMode.CREATIVE, Gamemode.CREATIVE);
        gamemodeMap.put(GameMode.SURVIVAL, Gamemode.SURVIVAL);
        gamemodeMap.put(GameMode.ADVENTURE, Gamemode.ADVENTURE);
        gamemodeMap.put(GameMode.SPECTATOR, Gamemode.SPECTATOR);
    }

    public BukkitPlayer(OfflinePlayer parent) {
        this.parent = parent;
    }

    @Override
    public UUID getUuid() {
        return parent.getUniqueId();
    }

    @Override
    public String getUsername() {
        return parent.getName();
    }

    @Override
    public boolean isOnline() {
        return parent.isOnline();
    }

    @Override
    public boolean hasPermission(String permission) {
        if (parent instanceof Player) {
            return ((Player) parent).hasPermission(permission);
        }
        return false;
    }

    @Override
    public Gamemode getGamemode() {
        if (parent instanceof Player) {
            return gamemodeMap.get(((Player) parent).getGameMode());
        }
        return null;
    }

    @Override
    public void setGamemode(Gamemode mode) {
        if (parent instanceof Player) {
            org.bukkit.GameMode bukkitMode = gamemodeMap.entrySet().stream()
                    .filter(k -> k.getValue() == mode)
                    .map(Map.Entry::getKey)
                    .findFirst().orElse(null);

            // both enums are fully mapped so this is safe
            Objects.requireNonNull(bukkitMode);
	        ((Player) parent).setGameMode(bukkitMode);
        }
    }
}
