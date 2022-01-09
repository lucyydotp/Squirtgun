package net.lucypoulton.squirtgun.minecraft.platform.event;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.lucypoulton.squirtgun.minecraft.platform.Platform;
import net.lucypoulton.squirtgun.minecraft.platform.event.cancellable.CancellableEvent;

import java.lang.reflect.Type;

/**
 * Manages the execution of events and listeners.
 */
public class EventManager {

    // mojang, can you please consider not using an ancient, obsolete version of guava
    @SuppressWarnings("UnstableApiUsage")
    private final Multimap<Type, EventHandler<?>> handlerMap = MultimapBuilder.hashKeys().treeSetValues().build();

    private final Platform platform;

    public EventManager(Platform platform) {
        this.platform = platform;
    }

    /**
     * Registers all of a listener's handlers.
     *
     * @param listener the listener to register
     */
    public void register(EventListener listener) {
        for (EventHandler<?> handler : listener.handlers()) {
            register(handler);
        }
    }

    /**
     * Registers an event handler.
     *
     * @param handler the handler to register
     */
    public void register(EventHandler<?> handler) {
        handlerMap.put(handler.eventType(), handler);
    }

    /**
     * Unregisters an event handler.
     *
     * @param handler the handler to unregister
     */
    public void unregister(EventHandler<?> handler) {
        handlerMap.remove(handler.eventType(), handler);
    }

    private <T extends Event> void attemptExecution(T event, EventHandler<T> handler) {
        try {
            handler.execute(event);
        } catch (Exception e) {
            platform.getLogger().severe("Exception thrown while handling " + event.getClass().getName() +
                ":\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Dispatches an event, calling all event handlers in the corresponding order.
     * If the event is cancellable, then {@link Event.Result#FAILURE} will be returned if it is cancelled.
     *
     * @param event the event object to dispatch.
     * @return a result indicating whether the event was cancelled.
     * If the event is not cancellable, then {@link Event.Result#SUCCESS}.
     */
    public <T extends Event> Event.Result dispatch(T event) {

        if (event instanceof CancellableEvent) {
            CancellableEvent cancellable = (CancellableEvent) event;
            // FIXME: this doesn't iterate over superclasses, is this something we want?
            for (EventHandler<?> handler : this.handlerMap.get(event.getClass())) {
                // cast is safe due to logic
                //noinspection unchecked
                EventHandler<T> handlerCasted = (EventHandler<T>) handler;
                if (cancellable.willContinue() || handlerCasted.executesOnCancel()) {
                    attemptExecution(event, handlerCasted);
                }
            }
            return Event.Result.ofCancellable(cancellable);
        }
        for (EventHandler<?> handler : this.handlerMap.get(event.getClass())) {

            // cast is safe due to logic
            //noinspection unchecked
            attemptExecution(event, (EventHandler<T>) handler);
        }
        return Event.Result.SUCCESS;
    }
}
