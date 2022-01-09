package net.lucypoulton.squirtgun.minecraft.platform.event;

import net.lucypoulton.squirtgun.minecraft.platform.event.cancellable.CancellableEvent;
import org.jetbrains.annotations.Nullable;

/**
 * An event.
 */
public interface Event {

    /**
     * The result of executing an event's handlers.
     */
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

        /**
         * Whether the handlers were successful.
         * If false, the event should be considered cancelled and
         * native execution should be stopped where possible.
         */
        boolean successful();

        /**
         * Inverse of {@link #successful()}
         */
        default boolean failed() {
            return !successful();
        }

        /**
         * The reason given for this result. May be null.
         */
        default @Nullable String reason() {
            return null;
        }
    }
}
