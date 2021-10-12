package net.lucypoulton.squirtgun.platform.event;

import net.lucypoulton.squirtgun.platform.event.cancellable.CancellableEvent;

/**
 * An event.
 */
public interface Event {
    interface Result {
        Result SUCCESS = () -> true;
        Result FAILURE = () -> false;

        static Result ofCancellable(CancellableEvent event) {
            return () -> !event.isCancelled();
        }

        boolean successful();
    }
}
