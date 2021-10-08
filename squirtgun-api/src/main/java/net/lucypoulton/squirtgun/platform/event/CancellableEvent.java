package net.lucypoulton.squirtgun.platform.event;

public interface CancellableEvent extends Event {
	void cancel();
}
