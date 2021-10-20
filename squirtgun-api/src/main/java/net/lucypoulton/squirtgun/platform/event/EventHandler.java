package net.lucypoulton.squirtgun.platform.event;

import com.google.common.reflect.TypeToken;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * An event handler.
 * This class is abstract to allow for internal type reflection magic -
 * create an anonymous class when constructing, for example {@code new EventHandler(...) {}}.
 * @param <T> the event to handle
 */
// blame the ancient version of guava minecraft uses.
@SuppressWarnings("UnstableApiUsage")
public abstract class EventHandler<T extends Event> {
    private final TypeToken<T> eventType;
    private final EventPriority priority;
    private final boolean executeOnCancel;
    private final Consumer<T> handlerMethod;

    private EventHandler(EventPriority priority, boolean executeOnCancel, Consumer<T> handlerMethod) {
        this.priority = priority;
        this.executeOnCancel = executeOnCancel;
        this.handlerMethod = handlerMethod;
        this.eventType = new TypeToken<>(getClass()) {};
    }

    /**
     * The type of event that this handler accepts.
     */
    @SuppressWarnings("unchecked")
    public Class<T> eventType() {
        return (Class<T>) eventType.getRawType();
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

    /**
     * Creates a new builder.
     * @param <T> the type of event to listen for
     * @return a new builder
     */
    public static <T extends Event> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Creates a new builder from a consumer, with event priority {@link EventPriority#NORMAL}.
     * @param handler the handler to execute
     * @param <T> the event to handle
     */
    public static <T extends Event> EventHandler<T> executes(Consumer<T> handler) {
        return new EventHandler<>(EventPriority.NORMAL, false, handler) { };
    }

    public static class Builder<T extends Event> {
        private EventPriority priority = EventPriority.NORMAL;
        private boolean executeOnCancel = false;
        private Consumer<T> handlerMethod;

        private Builder() {}

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
         * @throws NullPointerException if a required parameter ({@link #handle(Consumer)}) is missing
         */
        public EventHandler<T> build() {
            Objects.requireNonNull(handlerMethod);
            return new EventHandler<>(priority, executeOnCancel, handlerMethod) {};
        }
    }
}
