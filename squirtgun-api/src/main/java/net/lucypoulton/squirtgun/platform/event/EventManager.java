package net.lucypoulton.squirtgun.platform.event;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.lucypoulton.squirtgun.platform.event.cancellable.CancellableEvent;
import java.util.Comparator;

public class EventManager {

    // mojang, have you ever considered using a version of guava that isn't nearly 5 years old
    @SuppressWarnings("UnstableApiUsage")
    private final Multimap<Class<? extends Event>, EventHandler<?>> handlers = MultimapBuilder
            .hashKeys()
            .<EventHandler<?>>treeSetValues(Comparator.comparingInt(h -> h.priority().getLevel()))
            .build();

    public void register(EventListener listener) {
        for (EventHandler<?> handler : listener.handlers()) {
            register(handler);
        }
    }

    public void register(EventHandler<?> handler) {
        handlers.put(handler.eventType(), handler);
    }


    public void unregister(EventHandler<?> handler) {
        handlers.remove(handler.eventType(), handler);
    }

    private <T extends CancellableEvent> Event.Result dispatch(T event) {
        for (EventHandler<?> handler : this.handlers.get(event.getClass())) {
            // cast is safe due to logic
            //noinspection unchecked
            EventHandler<T> handlerCasted = (EventHandler<T>) handler;
            if (event.willContinue() || handlerCasted.executesOnCancel()) handlerCasted.execute(event);
        }
        return Event.Result.ofCancellable(event);
    }

    public <T extends Event> Event.Result dispatch(T event) {
        if (event instanceof CancellableEvent) {
            return dispatch((CancellableEvent) event);
        }
        for (EventHandler<?> handler : this.handlers.get(event.getClass())) {
            // cast is safe due to logic
            //noinspection unchecked
            ((EventHandler<T>) handler).execute(event);
        }
        return Event.Result.SUCCESS;
    }
}
