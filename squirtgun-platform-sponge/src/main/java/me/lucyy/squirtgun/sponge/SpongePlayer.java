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

package me.lucyy.squirtgun.sponge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.lucyy.squirtgun.platform.Gamemode;
import me.lucyy.squirtgun.platform.audience.SquirtgunPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import java.util.*;

/**
 * SquirtgunPlayer implementation for Bukkit.
 */
public class SpongePlayer implements SquirtgunPlayer, ForwardingAudience.Single {

    private final User user;
    private static final BiMap<Gamemode, GameMode> MODE_MAP = HashBiMap.create(4);

    static {
        MODE_MAP.put(Gamemode.CREATIVE, GameModes.CREATIVE.get());
        MODE_MAP.put(Gamemode.SURVIVAL, GameModes.SURVIVAL.get());
        MODE_MAP.put(Gamemode.SPECTATOR, GameModes.SPECTATOR.get());
        MODE_MAP.put(Gamemode.ADVENTURE, GameModes.ADVENTURE.get());
    }

    public SpongePlayer(User user) {
        this.user = user;
    }

    @Override
    public UUID getUuid() {
        return user.uniqueId();
    }

    @Override
    public String getUsername() {
        return user.name();
    }

    @Override
    public boolean isOnline() {
        return user.isOnline();
    }

    @Override
    public boolean hasPermission(String permission) {
        return user.hasPermission(permission);
    }

    @Override
    public Gamemode getGamemode() {
        Player player = user.player().orElse(null);
        return player == null ? null : MODE_MAP.inverse().get(player.gameMode().get());
    }

    @Override
    public void setGamemode(Gamemode mode) {
        user.player().ifPresent(p -> p.gameMode().set(MODE_MAP.get(mode)));
    }

    @Override
    public @NotNull Audience audience() {
        Audience audience = user.player().orElse(null);
        return audience == null ? Audience.empty() : audience;
    }
}
