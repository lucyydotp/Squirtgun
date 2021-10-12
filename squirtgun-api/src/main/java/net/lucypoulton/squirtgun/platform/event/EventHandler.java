package net.lucypoulton.squirtgun.platform.event;

import java.util.Objects;
import java.util.function.Consumer;

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

    public Class<T> eventType() {
        return eventType;
    }

    public EventPriority priority() {
        return priority;
    }

    public boolean executesOnCancel() {
        return executeOnCancel;
    }

    public void execute(T event) {
        handlerMethod.accept(event);
    }

    public static class Builder<T extends Event> {
        private Class<T> eventType;
        private EventPriority priority = EventPriority.NORMAL;
        private boolean executeOnCancel = false;
        private Consumer<T> handlerMethod;

        public Builder<T> eventType(Class<T> clazz) {
            this.eventType = clazz;
            return this;
        }

        public Builder<T> priority(EventPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder<T> executeOnCancel() {
            executeOnCancel = true;
            return this;
        }

        public Builder<T> handle(Consumer<T> method) {
            this.handlerMethod = method;
            return this;
        }

        public EventHandler<T> build() {
            Objects.requireNonNull(eventType);
            Objects.requireNonNull(handlerMethod);
            return new EventHandler<>(eventType, priority, executeOnCancel, handlerMethod);
        }
    }
}
