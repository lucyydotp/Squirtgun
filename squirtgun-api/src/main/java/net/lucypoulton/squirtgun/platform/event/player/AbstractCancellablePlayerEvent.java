package net.lucypoulton.squirtgun.platform.event.player;

import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.platform.event.cancellable.AbstractCancellableEvent;
import org.jetbrains.annotations.NotNull;

public class AbstractCancellablePlayerEvent extends AbstractCancellableEvent implements PlayerEvent {

    private final SquirtgunPlayer player;

    public AbstractCancellablePlayerEvent(SquirtgunPlayer player) {
        this.player = player;
    }

    @Override
    public @NotNull SquirtgunPlayer player() {
        return player;
    }
}
