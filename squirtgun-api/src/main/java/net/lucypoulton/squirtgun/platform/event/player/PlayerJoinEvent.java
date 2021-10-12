package net.lucypoulton.squirtgun.platform.event.player;

import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;

public class PlayerJoinEvent extends AbstractCancellablePlayerEvent {
    public PlayerJoinEvent(SquirtgunPlayer player) {
        super(player);
    }
}
