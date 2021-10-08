package net.lucypoulton.squirtgun.platform.event;

import java.util.function.Consumer;

public interface EventListener {

	interface RegisterContext {
		default <T extends Event> void register(Class<T> event, Consumer<T> handler) {
			register(event, EventPriority.NORMAL, handler);
		}

		 <T extends Event> void register(Class<T> event, EventPriority priority, Consumer<T> handler);
	}

	void setup(RegisterContext context);
}
