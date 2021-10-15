package net.lucypoulton.squirtgun.platform.event;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * An event handler.
 * @param <T> the event to handle
 */
public class EventHandler<T extends Event> {
    private final Class<T> eventType;
    private final EventPriority priority;
    private final boolean executeOnCancel;
    private final Consumer<T> handlerMethod;

    public EventHandler(Class<T> eventType, EventPriority priority, boolean executeOnCancel, Consumer<T> handlerMethod) {
        this.eventType = eventType;
        this.priority = priority;
        this.executeOnCancel = executeOnCancel;
        this.handlerMethod = handlerMethod;
    }

    /**
     * The type of event that this handler accepts.
     */
    public Class<T> eventType() {
        return eventType;
    }

    /**
     * This handler's priority.
     */
    public EventPriority priority() {
        return priority;
    }

    /**
     * Whether this handler should be called if a handler with a higher priority has cancelled the event.
     * This value is not used with events that aren't cancellable.
     */
    public boolean executesOnCancel() {
        return executeOnCancel;
    }

    /**
     * Executes the handler.
     */
    public void execute(T event) {
        handlerMethod.accept(event);
    }

    public static class Builder<T extends Event> {
        private Class<T> eventType;
        private EventPriority priority = EventPriority.NORMAL;
        private boolean executeOnCancel = false;
        private Consumer<T> handlerMethod;

        /**
         * This is a required parameter.
         * @see EventHandler#eventType()
         */
        public Builder<T> eventType(Class<T> clazz) {
            this.eventType = clazz;
            return this;
        }

        /**
         * Defaults to {@link EventPriority#NORMAL}.
         * @see EventHandler#priority()
         */
        public Builder<T> priority(EventPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Defaults to false, calling sets to true.
         * @see EventHandler#executesOnCancel()
         */
        public Builder<T> executeOnCancel() {
            executeOnCancel = true;
            return this;
        }

        /**
         * This is a required parameter.
         * @see EventHandler#execute(Event)
         */
        public Builder<T> handle(Consumer<T> method) {
            this.handlerMethod = method;
            return this;
        }

        /**
         * Builds an EventHandler.
         * @throws NullPointerException if a required parameter ({@link #handle(Consumer)} or {@link #eventType(Class)}) is missing
         */
        public EventHandler<T> build() {
            Objects.requireNonNull(eventType);
            Objects.requireNonNull(handlerMethod);
            return new EventHandler<>(eventType, priority, executeOnCancel, handlerMethod);
        }
    }
}
