package net.lucypoulton.squirtgun.platform.event.player;

import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveEvent implements PlayerEvent {

    private final SquirtgunPlayer player;

    public PlayerLeaveEvent(@NotNull SquirtgunPlayer player) {
        this.player = player;
    }

    @Override
    public @NotNull SquirtgunPlayer player() {
        return player;
    }
}
