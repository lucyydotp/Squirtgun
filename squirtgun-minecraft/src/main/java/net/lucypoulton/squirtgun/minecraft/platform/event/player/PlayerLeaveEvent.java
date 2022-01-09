package net.lucypoulton.squirtgun.minecraft.platform.event.player;

import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * An event called when a player leaves the server.
 */
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
