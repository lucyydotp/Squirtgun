package net.lucypoulton.squirtgun.minecraft.platform.event.player;

import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.minecraft.platform.event.cancellable.CancellableEvent;
import net.lucypoulton.squirtgun.minecraft.platform.event.cancellable.AbstractCancellableEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract event, implementing {@link PlayerEvent#player()} and {@link CancellableEvent} methods.
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
