package net.lucypoulton.squirtgun.platform.event.player;

import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.platform.event.cancellable.AbstractCancellableEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract event, implementing {@link PlayerEvent#player()} and {@link net.lucypoulton.squirtgun.platform.event.cancellable.CancellableEvent} methods.
 */
public abstract class AbstractCancellablePlayerEvent extends AbstractCancellableEvent implements PlayerEvent {

    private final SquirtgunPlayer player;

    public AbstractCancellablePlayerEvent(SquirtgunPlayer player) {
        this.player = player;
    }

    @Override
    public @NotNull SquirtgunPlayer player() {
        return player;
    }
}
