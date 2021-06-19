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

package me.lucyy.squirtgun.sponge;

import me.lucyy.squirtgun.sponge.task.SpongeTaskScheduler;
import me.lucyy.squirtgun.platform.AuthMode;
import me.lucyy.squirtgun.platform.EventListener;
import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.audience.SquirtgunUser;
import me.lucyy.squirtgun.platform.audience.SquirtgunPlayer;
import me.lucyy.squirtgun.platform.scheduler.TaskScheduler;
import me.lucyy.squirtgun.plugin.SquirtgunPlugin;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.plugin.PluginContainer;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A Platform implementation for Sponge.
 */
public class SpongePlatform implements Platform {

    private final PluginContainer plugin;
    private final Path configPath;

    private final SpongeTaskScheduler scheduler = new SpongeTaskScheduler(this);

    private final SpongeListenerAdapter listenerAdapter = new SpongeListenerAdapter();

    public SpongePlatform(final PluginContainer plugin, final Path configPath) {
        this.plugin = plugin;
        this.configPath = configPath;
        org.spongepowered.api.Sponge.eventManager().registerListeners(plugin, new SpongeListenerAdapter());
    }

    public PluginContainer getPlugin() {
        return plugin;
    }

    @Override
    public String name() {
        return "Sponge";
    }

    @Override
    public Logger getLogger() {
        //return plugin.logger();
        return null; // TODO
    }

    @Override
    public void log(Component component) {
        org.spongepowered.api.Sponge.server().sendMessage(component);
    }


    @Override
    public AuthMode getAuthMode() {
        return org.spongepowered.api.Sponge.server().isOnlineModeEnabled() ? AuthMode.ONLINE : AuthMode.OFFLINE;
    }

    @Override
    public TaskScheduler getTaskScheduler() {
        return scheduler;
    }

    @Override
    public void registerEventListener(EventListener listener) {
        listenerAdapter.addListener(listener);
    }

    @Override
    public void unregisterEventListener(EventListener listener) {
        listenerAdapter.removeListener(listener);
    }

    @Override
    public SquirtgunUser getConsole() {
        return SpongeConsoleWrapper.instance;
    }

    @Override
    public SquirtgunPlayer getPlayer(UUID uuid) {
        User player = Sponge.server().userManager().find(uuid).orElse(null);
        return player == null ? null : new SpongePlayer(player);
    }

    @Override
    public SquirtgunPlayer getPlayer(String name) {
        User player = Sponge.server().userManager().find(name).orElse(null);
        return player == null ? null : new SpongePlayer(player);
    }

    @Override
    public List<SquirtgunPlayer> getOnlinePlayers() {
        return Sponge.server().onlinePlayers().stream()
                .map(p -> new SpongePlayer(p.user()))
                .collect(Collectors.toList());
    }

    /**
     * Gets a SquirtgunUser from a command cause.
     *
     * @param cause the command cause
     */
    public SquirtgunUser getUser(CommandCause cause) {
        if (cause instanceof Player) {
            return getPlayer(((Player) cause).uniqueId());
        }
        return getConsole();
    }

    @Override
    public Path getConfigPath(SquirtgunPlugin<?> plugin) {
        return configPath;
    }
}
