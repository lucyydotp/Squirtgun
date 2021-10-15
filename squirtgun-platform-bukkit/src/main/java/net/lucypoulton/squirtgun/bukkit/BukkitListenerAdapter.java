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

import net.lucypoulton.squirtgun.platform.EventListener;
import net.lucypoulton.squirtgun.platform.event.Event;
import net.lucypoulton.squirtgun.platform.event.cancellable.CancellableEvent;
import net.lucypoulton.squirtgun.platform.event.player.PlayerJoinEvent;
import net.lucypoulton.squirtgun.platform.event.player.PlayerLeaveEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class BukkitListenerAdapter implements Listener {

    private final BukkitPlatform platform;

    public BukkitListenerAdapter(BukkitPlatform platform) {
        this.platform = platform;
    }

    private void handleCancellable(Cancellable bukkitEvent, CancellableEvent squirtgunEvent) {
        Event.Result result = platform.getEventManager().dispatch(squirtgunEvent);
        if (result.failed()) {
            bukkitEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerLoginEvent e) {
        PlayerJoinEvent sgEvent = new PlayerJoinEvent(platform.getPlayer(e.getPlayer().getUniqueId()));
        Event.Result result = platform.getEventManager().dispatch(sgEvent);
        if (result.failed()) {
            String reason = result.reason();
            if (reason == null) {
                reason = "";
            }
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, reason);
        }
    }

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent e) {
        platform.getEventManager().dispatch(new PlayerLeaveEvent(platform.getPlayer(e.getPlayer().getUniqueId())));
    }
}
