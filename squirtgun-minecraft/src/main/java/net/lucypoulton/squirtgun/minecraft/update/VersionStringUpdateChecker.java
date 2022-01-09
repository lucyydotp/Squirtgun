package net.lucypoulton.squirtgun.minecraft.update;

import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.minecraft.plugin.SquirtgunPlugin;

/**
 * Update checking mechanism that compares a string fetched from a URL with the current version.
 */
public class VersionStringUpdateChecker extends UpdateChecker {
    protected VersionStringUpdateChecker(SquirtgunPlugin<?> plugin, String url, Component updateMessage, String listenerPermission) {
        super(plugin, url, updateMessage, listenerPermission);
    }

    @Override
    protected boolean checkDataForUpdate(String input) {
        return !getPlugin().getPluginVersion().toString().equals(input);
    }
}
