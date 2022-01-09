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

package net.lucypoulton.squirtgun.bukkit;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.lucypoulton.squirtgun.minecraft.platform.Gamemode;
import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunPlayer;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * SquirtgunPlayer implementation for Bukkit.
 */
public class BukkitPlayer implements SquirtgunPlayer, ForwardingAudience.Single {

    private static final EnumMap<org.bukkit.GameMode, Gamemode> gamemodeMap = new EnumMap<>(org.bukkit.GameMode.class);

    static {
        gamemodeMap.put(GameMode.CREATIVE, Gamemode.CREATIVE);
        gamemodeMap.put(GameMode.SURVIVAL, Gamemode.SURVIVAL);
        gamemodeMap.put(GameMode.ADVENTURE, Gamemode.ADVENTURE);
        gamemodeMap.put(GameMode.SPECTATOR, Gamemode.SPECTATOR);
    }

    private final OfflinePlayer parent;
    private final Audience audience;

    public BukkitPlayer(OfflinePlayer parent, Audience audience) {
        this.parent = parent;
        this.audience = audience;
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

    @Override
    public @NotNull Audience audience() {
        return audience;
    }
}
