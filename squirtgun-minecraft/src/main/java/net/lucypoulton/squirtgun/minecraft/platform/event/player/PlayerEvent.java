package net.lucypoulton.squirtgun.minecraft.platform.event.player;

import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.minecraft.platform.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * An event with a player argument.
 */
public interface PlayerEvent extends Event {
    @NotNull
    SquirtgunPlayer player();
}
