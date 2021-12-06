package net.lucypoulton.squirtgun.platform.event.player;

import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;

public class PlayerChatEvent extends AbstractCancellablePlayerEvent {

    private final String message;

    public PlayerChatEvent(SquirtgunPlayer player, String message) {
        super(player);
        this.message = message;
    }

    public String message() {
        return message;
    }
}
