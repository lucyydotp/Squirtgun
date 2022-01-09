package net.lucypoulton.squirtgun.minecraft.platform.event.player;

import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunPlayer;

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
