package net.lucypoulton.squirtgun.minecraft.platform.event.player;

import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunPlayer;

/**
 * An event called when a player joins the server.
 */
public class PlayerJoinEvent extends AbstractCancellablePlayerEvent {
    public PlayerJoinEvent(SquirtgunPlayer player) {
        super(player);
    }
}
