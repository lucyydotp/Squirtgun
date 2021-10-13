package net.lucypoulton.squirtgun.platform.event;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.event.cancellable.CancellableEvent;
import java.util.Comparator;

public class EventManager {

    // mojang, have you ever considered using a version of guava that isn't nearly 5 years old
    @SuppressWarnings("UnstableApiUsage")
    private final Multimap<Class<? extends Event>, EventHandler<?>> handlers = MultimapBuilder
            .hashKeys()
            .<EventHandler<?>>treeSetValues(Comparator.comparingInt(h -> h.priority().getLevel()))
            .build();

    private final Platform platform;

    public EventManager(Platform platform) {
        this.platform = platform;
    }

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

    public <T extends Event> Event.Result dispatch(T event) {

        if (event instanceof CancellableEvent) {
            CancellableEvent cancellable = (CancellableEvent) event;
            // FIXME: this doesn't iterate over superclasses, is this something we want?
            for (EventHandler<?> handler : this.handlers.get(event.getClass())) {
                // cast is safe due to logic
                //noinspection unchecked
                EventHandler<T> handlerCasted = (EventHandler<T>) handler;
                if (cancellable.willContinue() || handlerCasted.executesOnCancel()) {
                    handlerCasted.execute(event);
                }
            }
            return Event.Result.ofCancellable(cancellable);
        }
        for (EventHandler<?> handler : this.handlers.get(event.getClass())) {
            try {
                // cast is safe due to logic
                //noinspection unchecked
                ((EventHandler<T>) handler).execute(event);
            } catch (Exception e) {
                platform.getLogger().severe("Exception thrown while handling " + event.getClass().getName() +
                        ":\n" + e.getMessage());
                e.printStackTrace();
            }
        }
        return Event.Result.SUCCESS;
    }
}
