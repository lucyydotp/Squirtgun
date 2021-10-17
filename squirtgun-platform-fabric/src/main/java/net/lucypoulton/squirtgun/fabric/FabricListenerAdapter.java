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
import net.lucypoulton.squirtgun.platform.event.Event;
import net.lucypoulton.squirtgun.platform.event.player.PlayerJoinEvent;
import net.lucypoulton.squirtgun.platform.event.player.PlayerLeaveEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;

class FabricListenerAdapter {

    private final FabricPlatform platform;

    FabricListenerAdapter() {
        ServerPlayConnectionEvents.JOIN.register(this::playerJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(this::playerDisconnect);
    }

    FabricListenerAdapter(FabricPlatform platform) {
        this.platform = platform;
    }

    private void playerJoin(final ServerPlayNetworkHandler handler, final PacketSender sender, final MinecraftServer server) {
        PlayerJoinEvent sgEvent = new PlayerJoinEvent(platform.getPlayer(handler.getPlayer()));
        Event.Result result = platform.getEventManager().dispatch(sgEvent);
        if (result.failed()) {
            String reason = result.reason();
            if (reason == null) {
                reason = "";
            }
            handler.disconnect(Text.of(reason));
        }
    }

    private void playerDisconnect(final ServerPlayNetworkHandler handler, final MinecraftServer server) {
        platform.getEventManager().dispatch(new PlayerLeaveEvent(platform.getPlayer(handler.getPlayer())));
    }
}
