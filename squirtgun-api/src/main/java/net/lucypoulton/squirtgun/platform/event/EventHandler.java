package net.lucypoulton.squirtgun.platform.event;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

/**
 * An event handler.
 *
 * @param <T> the event to handle
 */
public class EventHandler<T extends Event> implements Comparable<EventHandler<?>> {
    private final Class<T> eventType;
    private final EventPriority priority;
    private final boolean executeOnCancel;
    private final Consumer<T> handlerMethod;

    /* this field is used to give the comparator something other than priority to compare.
    without it, we end up with handlers that are equal according to the comparator, but not
    according to Object.equals and sorted sets don't like that. */
    private final int id;

    private EventHandler(int id, Class<T> eventType, EventPriority priority, boolean executeOnCancel, Consumer<T> handlerMethod) {
        this.id = id;
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


    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventHandler && ((EventHandler<?>) obj).id == this.id;
    }

    @Override
    public int compareTo(@NotNull EventHandler<?> o) {
        int byPriority = this.priority().getLevel() - o.priority().getLevel();
        return byPriority == 0 ? this.id - o.id : byPriority;
    }

    private static final Random random = new Random();

    /**
     * Creates a new builder.
     *
     * @param <T> the type of event to listen for
     * @return a new builder
     */
    public static <T extends Event> Builder<T> builder(Class<T> eventType) {
        return new Builder<>(eventType);
    }

    /**
     * Creates a new builder from a consumer, with event priority {@link EventPriority#NORMAL}.
     *
     * @param handler the handler to execute
     * @param <T>     the event to handle
     */
    public static <T extends Event> EventHandler<T> executes(Class<T> event, Consumer<T> handler) {
        return new EventHandler<>(random.nextInt(), event, EventPriority.NORMAL, false, handler) {
        };
    }

    public static class Builder<T extends Event> {
        private final Class<T> eventType;
        private EventPriority priority = EventPriority.NORMAL;
        private boolean executeOnCancel = false;
        private Consumer<T> handlerMethod;

        private Builder(Class<T> eventType) {
            this.eventType = eventType;
        }

        /**
         * Defaults to {@link EventPriority#NORMAL}.
         *
         * @see EventHandler#priority()
         */
        public Builder<T> priority(EventPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Defaults to false, calling sets to true.
         *
         * @see EventHandler#executesOnCancel()
         */
        public Builder<T> executeOnCancel() {
            executeOnCancel = true;
            return this;
        }

        /**
         * This is a required parameter.
         *
         * @see EventHandler#execute(Event)
         */
        public Builder<T> handle(Consumer<T> method) {
            this.handlerMethod = method;
            return this;
        }

        /**
         * Builds an EventHandler.
         *
         * @throws NullPointerException if a required parameter ({@link #handle(Consumer)}) is missing
         */
        public EventHandler<T> build() {
            Objects.requireNonNull(handlerMethod);
            return new EventHandler<>(random.nextInt(), eventType, priority, executeOnCancel, handlerMethod) {
            };
        }
    }
}
