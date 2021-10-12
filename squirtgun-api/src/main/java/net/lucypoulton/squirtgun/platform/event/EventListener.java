package net.lucypoulton.squirtgun.platform.event;

import java.util.List;

public interface EventListener {
    List<EventHandler<?>> handlers();
}
