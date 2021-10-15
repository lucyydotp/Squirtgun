package net.lucypoulton.squirtgun.platform.event.cancellable;

import net.lucypoulton.squirtgun.platform.event.Event;

public interface CancellableEvent extends Event {
    /**
     * Cancels this event.
     */
    void cancel();

    /**
     * Inverse of {@link #isCancelled()}
     */
    default boolean willContinue() {
        return !isCancelled();
    }

    /**
     * Whether the event has been cancelled by a handler.
     */
    boolean isCancelled();
}
