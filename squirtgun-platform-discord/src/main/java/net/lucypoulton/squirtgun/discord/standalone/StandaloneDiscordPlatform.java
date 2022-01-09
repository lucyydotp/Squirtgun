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
package net.lucypoulton.squirtgun.discord.standalone;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.lucypoulton.squirtgun.discord.DiscordPlatform;
import net.lucypoulton.squirtgun.discord.DiscordUser;
import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunUser;
import net.lucypoulton.squirtgun.minecraft.platform.event.EventManager;
import net.lucypoulton.squirtgun.minecraft.platform.scheduler.TaskScheduler;
import net.lucypoulton.squirtgun.minecraft.plugin.SquirtgunPlugin;
import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * A Platform implementation for standalone Discord bots.
 */
public class StandaloneDiscordPlatform extends DiscordPlatform {

    private final TaskScheduler scheduler = new StandaloneTaskScheduler(this);
    private final SquirtgunUser console = new StandaloneConsoleWrapper(this);
    private final EventManager manager = new EventManager(this);

    public StandaloneDiscordPlatform(JDA jda, String commandPrefix) {
        super(jda, commandPrefix);
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }

    @Override
    public DiscordUser getPlayer(UUID uuid) {
        if (uuid.getMostSignificantBits() != 0L) return null;
        return new StandaloneDiscordUser(jda().getUserById(uuid.getLeastSignificantBits()));
    }

    /**
     * Fetches a user by their Discord username and discriminator, in the format {@code username#0000},
     * where the username must be between 2 and 32 characters (inclusive) matching the exact casing
     * and the discriminator must be exactly 4 digits.
     */
    @Override
    public @Nullable DiscordUser getPlayer(String name) {
        User user = jda().getUserByTag(name);
        if (user == null) {
            return null;
        }
        return audiences().user(user);
    }

    @Override
    public Logger getLogger() {
        return Logger.getGlobal();
    }

    @Override
    public TaskScheduler getTaskScheduler() {
        return scheduler;
    }

    @Override
    public EventManager getEventManager() {
        return manager;
    }

    @Override
    public SquirtgunUser getConsole() {
        return console;
    }

    @Override
    public Path getConfigPath(SquirtgunPlugin<?> plugin) {
        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            // up one for the directory
            return Path.of(url.toURI()).getParent();
        } catch (URISyntaxException e) {
            // this shouldn't happen
            e.printStackTrace();
        }
        return null;
    }
}
