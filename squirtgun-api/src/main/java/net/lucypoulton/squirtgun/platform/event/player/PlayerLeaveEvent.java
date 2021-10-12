package net.lucypoulton.squirtgun.platform.event.player;

import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveEvent extends AbstractCancellablePlayerEvent {
    public PlayerLeaveEvent(@NotNull SquirtgunPlayer player) {
        super(player);
    }
}
