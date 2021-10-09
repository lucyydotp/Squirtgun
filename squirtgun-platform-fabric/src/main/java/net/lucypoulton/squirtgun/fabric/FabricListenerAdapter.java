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

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.lucypoulton.squirtgun.platform.EventListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.UUID;

class FabricListenerAdapter {

    private final Set<EventListener> listeners = Collections.newSetFromMap(new IdentityHashMap<>());

    FabricListenerAdapter() {
        ServerPlayConnectionEvents.JOIN.register(this::playerJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(this::playerDisconnect);
    }

    void addListener(final EventListener listener) {
        this.listeners.add(listener);
    }

    void removeListener(final EventListener listener) {
        this.listeners.remove(listener);
    }

    private void playerJoin(final ServerPlayNetworkHandler handler, final PacketSender sender, final MinecraftServer server) {
        final UUID uuid = handler.player.getUuid();
        this.listeners.forEach(listener -> listener.onPlayerJoin(uuid));
    }

    private void playerDisconnect(final ServerPlayNetworkHandler handler, final MinecraftServer server) {
        final UUID uuid = handler.player.getUuid();
        this.listeners.forEach(listener -> listener.onPlayerLeave(uuid));
    }
}
