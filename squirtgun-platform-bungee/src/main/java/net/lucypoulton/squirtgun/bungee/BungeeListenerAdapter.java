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

import net.lucypoulton.squirtgun.platform.event.Event;
import net.lucypoulton.squirtgun.platform.event.player.PlayerJoinEvent;
import net.lucypoulton.squirtgun.platform.event.player.PlayerLeaveEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

class BungeeListenerAdapter implements Listener {

    private final BungeePlatform platform;

    BungeeListenerAdapter(BungeePlatform platform) {
        this.platform = platform;
    }

    @EventHandler
    public void onPlayerJoin(LoginEvent e) {
        PlayerJoinEvent sgEvent = new PlayerJoinEvent(platform.getPlayer(e.getConnection().getUniqueId()));
        Event.Result result = platform.getEventManager().dispatch(sgEvent);
        if (result.failed()) {
            String reason = result.reason();
            if (reason == null) {
                reason = "";
            }
            e.setCancelled(true);
            e.setCancelReason(TextComponent.fromLegacyText(reason));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent e) {
        platform.getEventManager().dispatch(new PlayerLeaveEvent(platform.getPlayer(e.getPlayer().getUniqueId())));
    }
}
