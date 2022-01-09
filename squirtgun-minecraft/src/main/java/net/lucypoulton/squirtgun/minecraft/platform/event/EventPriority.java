package net.lucypoulton.squirtgun.minecraft.platform.event;

/**
 * A priority for an event handler to dictate which order event handlers are executed in.
 */
public enum EventPriority {
    HIGHEST(0),
    HIGH(1),
    NORMAL(2),
    LOW(3),
    LOWEST(4);

    private final int level;

    EventPriority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
