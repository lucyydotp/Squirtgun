package net.lucypoulton.squirtgun.platform.event;

import java.util.List;

/**
 * A set of {@link EventHandler}s.
 */
public interface EventListener {
    List<EventHandler<?>> handlers();
}
