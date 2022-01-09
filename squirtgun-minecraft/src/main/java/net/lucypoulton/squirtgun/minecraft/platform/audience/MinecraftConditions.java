package net.lucypoulton.squirtgun.minecraft.platform.audience;

import net.lucypoulton.squirtgun.command.PermissionHolder;
import net.lucypoulton.squirtgun.command.condition.Condition;

public class MinecraftConditions {
    /**
     * A condition ensuring the target is a {@link SquirtgunPlayer}.
     */
    public static Condition<PermissionHolder, SquirtgunPlayer> isPlayer() {
        return (target, context) ->
                target instanceof SquirtgunPlayer
                        ? new Condition.Result<>(true, (SquirtgunPlayer) target, null)
                        : new Condition.Result<>(false, null, "This command can only be run by a player.");
    }

    /**
     * A condition ensuring the target is the console.
     */
    public static Condition<PermissionHolder, PermissionHolder> isConsole() {
        return (target, context) ->
                target instanceof SquirtgunPlayer
                        ? new Condition.Result<>(false, null, "This command can only be run from the console.")
                        : new Condition.Result<>(true, target, null);
    }
}
