package net.lucypoulton.squirtgun.platform.event;

import net.lucypoulton.squirtgun.platform.event.cancellable.CancellableEvent;
import org.jetbrains.annotations.Nullable;

/**
 * An event.
 */
public interface Event {
    @FunctionalInterface
    interface Result {
        Result SUCCESS = () -> true;
        Result FAILURE = () -> false;

        static Result ofCancellable(CancellableEvent event) {
            return () -> !event.isCancelled();
        }

        static Result withReason(boolean success, String reason) {
            return new Result() {
                @Override
                public boolean successful() {
                    return success;
                }

                @Override
                public @Nullable String reason() {
                    return reason;
                }
            };
        }

        boolean successful();

        default boolean failed() {
            return !successful();
        }

        default @Nullable String reason() {
            return null;
        }
    }
}
