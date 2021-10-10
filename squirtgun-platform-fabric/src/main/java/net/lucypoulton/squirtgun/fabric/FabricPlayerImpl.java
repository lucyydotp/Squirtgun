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

package net.lucypoulton.squirtgun.fabric;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumBiMap;
import com.google.common.collect.Maps;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.kyori.adventure.audience.Audience;
import net.lucypoulton.squirtgun.platform.Gamemode;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

class FabricPlayerImpl implements FabricPlayer {

    private static final BiMap<GameMode, Gamemode> GAMEMODE_BIMAP;

    static {
        final BiMap<GameMode, Gamemode> gamemodeBiMap = EnumBiMap.create(GameMode.class, Gamemode.class);
        gamemodeBiMap.put(GameMode.SURVIVAL, Gamemode.SURVIVAL);
        gamemodeBiMap.put(GameMode.CREATIVE, Gamemode.CREATIVE);
        gamemodeBiMap.put(GameMode.ADVENTURE, Gamemode.ADVENTURE);
        gamemodeBiMap.put(GameMode.SPECTATOR, Gamemode.SPECTATOR);
        GAMEMODE_BIMAP = Maps.unmodifiableBiMap(gamemodeBiMap);
    }

    private final ServerPlayerEntity handle;
    private final Audience audience;

    FabricPlayerImpl(final ServerPlayerEntity handle, final Audience audience) {
        this.handle = handle;
        this.audience = audience;
    }

    @Override
    public UUID getUuid() {
        return this.handle.getUuid();
    }

    @Override
    public String getUsername() {
        return this.handle.getEntityName();
    }

    @Override
    public boolean isOnline() {
        // a "player" is always online to be a player - TODO use GameProfile and NbtCompound?
        return true;
    }

    @Override
    public boolean hasPermission(final String permission) {
        return Permissions.check(this.handle, permission, 3);  // 3 = op level 3 (2nd to last, "admins")
    }

    @Override
    public Gamemode getGamemode() {
        return GAMEMODE_BIMAP.get(this.handle.interactionManager.getGameMode());
    }

    @Override
    public void setGamemode(final Gamemode mode) {
        this.handle.changeGameMode(GAMEMODE_BIMAP.inverse().get(mode));
    }

    @Override
    public @NotNull Audience audience() {
        return this.audience;
    }
}
