package net.lucypoulton.squirtgun.platform.event.cancellable;

import net.lucypoulton.squirtgun.platform.event.Event;

public interface CancellableEvent extends Event {
    void cancel();

    /**
     * Inversion of {@link #isCancelled()}
     */
    default boolean willContinue() {
        return !isCancelled();
    }

    boolean isCancelled();
}
