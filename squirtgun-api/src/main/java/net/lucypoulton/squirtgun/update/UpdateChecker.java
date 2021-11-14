/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.lucypoulton.squirtgun.update;

import net.lucypoulton.squirtgun.platform.event.EventHandler;
import net.lucypoulton.squirtgun.platform.event.player.PlayerJoinEvent;
import net.lucypoulton.squirtgun.platform.scheduler.Task;
import net.lucypoulton.squirtgun.plugin.SquirtgunPlugin;
import net.kyori.adventure.text.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Update checking mechanism base class.
 */
public abstract class UpdateChecker {
    private final SquirtgunPlugin<?> plugin;
    private final Component updateMessage;
    private final String listenerPermission;
    private final String url;
    private final Task listenerTask;
    private boolean updateAvailable;

    /**
     * Creates a new update checker, and schedule update checking every 3 hours.
     *
     * @param plugin             the plugin to check against
     * @param url                a URL (such as the spigot api) that will return a plaintext version string when retrieved
     * @param updateMessage      the message to show in console and to players with the listener permission on join
     * @param listenerPermission if a player holds this permission and an update is available, they will be sent the update message in chat
     */
    protected UpdateChecker(SquirtgunPlugin<?> plugin, String url, Component updateMessage, String listenerPermission) {
        this.plugin = plugin;
        this.url = url;
        this.updateMessage = updateMessage;
        this.listenerPermission = listenerPermission;

        EventHandler<PlayerJoinEvent> joinEventHandler = EventHandler.builder(PlayerJoinEvent.class)
                        .handle(e -> {
                            if (checkDataForUpdate() && e.player().hasPermission(getListenerPermission())) {
                                e.player().sendMessage(getUpdateMessage());
                            }
                        }).build();

        plugin.getPlatform().getEventManager().register(joinEventHandler);



        if (Arrays.stream(plugin.getPluginVersion().prerelease())
            .anyMatch(x -> x.toUpperCase(Locale.ROOT).contains("SNAPSHOT"))) {
            plugin.getPlatform().getLogger().warning("Development version detected, skipping update check.");
            listenerTask = null;
            return;
        }

        listenerTask = Task.builder()
                .async()
                .interval(216000)   // every 3 hours
                .action(x -> checkForUpdate())
                .build();

        plugin.getPlatform().getTaskScheduler().start(listenerTask);
    }

    /**
     * Check if the result of a request indicates an update is available.
     *
     * @param input the string result of a HTTP GET request from the URL provided to the constructor
     * @return whether the data provided shows an update available. In the event of an error, a warning
     * should be printed to the console and this should return false.
     */
    protected abstract boolean checkDataForUpdate(String input);

    /**
     * Gets the plugin that this checker is associated with.
     */
    protected SquirtgunPlugin<?> getPlugin() {
        return plugin;
    }

    /**
     * Executes a blocking request to check for an update. In the event of failure, a warning
     * will be printed to the console.
     *
     * @return whether an update is available. Should the check fail, returns false
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean checkForUpdate() {
        try {
            plugin.log("Checking for updates...");

            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            if (con.getResponseCode() != 200) throw new Exception();

            String text = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)
            ).lines().collect(Collectors.joining("\n"));

            if (checkDataForUpdate(text)) {
                updateAvailable = true;
                plugin.getPlatform().log(getUpdateMessage());
                plugin.getPlatform().getTaskScheduler().cancel(listenerTask);
                return true;
            } else plugin.getPlatform().getLogger().info("No update available.");

        } catch (Exception ignored) {
            plugin.getPlatform().getLogger().warning("Unable to check for updates!");
        }
        return false;
    }

    /**
     * @return the update message as set in the constructor
     */
    public Component getUpdateMessage() {
        return updateMessage;
    }

    /**
     * @return whether there is an update available. Note that this will not check for updates,
     * only return a cached result.
     */
    public boolean checkDataForUpdate() {
        return updateAvailable;
    }

    /**
     * @return the listener permission as set in the constructor
     */
    public String getListenerPermission() {
        return listenerPermission;
    }
}
