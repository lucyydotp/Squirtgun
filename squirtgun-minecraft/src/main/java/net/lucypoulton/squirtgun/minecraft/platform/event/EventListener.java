package net.lucypoulton.squirtgun.minecraft.platform.event;

import java.util.List;

/**
 * A set of {@link EventHandler}s.
 */
public interface EventListener {
    List<EventHandler<?>> handlers();
}
