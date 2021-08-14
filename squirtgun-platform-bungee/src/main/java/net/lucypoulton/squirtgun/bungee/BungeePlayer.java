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

package net.lucypoulton.squirtgun.bungee;

import net.lucypoulton.squirtgun.platform.Gamemode;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * SquirtgunPlayer implementation for Bukkit.
 */
public class BungeePlayer implements SquirtgunPlayer, ForwardingAudience.Single {

    private final ProxiedPlayer parent;
    private final Audience audience;

    public BungeePlayer(ProxiedPlayer parent, Audience audience) {
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
        return parent.isConnected();
    }

    @Override
    public boolean hasPermission(String permission) {
        return parent.hasPermission(permission);
    }

    @Override
    public Gamemode getGamemode() {
        throw new UnsupportedOperationException("Gamemodes cannot be accessed from BungeeCord");
    }

    @Override
    public void setGamemode(Gamemode mode) {
        throw new UnsupportedOperationException("Gamemodes cannot be accessed from BungeeCord");
    }

    @Override
    public @NotNull Audience audience() {
        return audience;
    }
}
