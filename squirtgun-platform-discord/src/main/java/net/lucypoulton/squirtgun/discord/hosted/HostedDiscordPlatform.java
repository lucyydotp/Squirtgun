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
package net.lucypoulton.squirtgun.discord.hosted;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.lucypoulton.squirtgun.discord.DiscordPlatform;
import net.lucypoulton.squirtgun.discord.DiscordUser;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunUser;
import net.lucypoulton.squirtgun.platform.scheduler.TaskScheduler;
import net.lucypoulton.squirtgun.plugin.SquirtgunPlugin;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class HostedDiscordPlatform extends DiscordPlatform {

    private final Platform parent;
    private final DiscordLinkHandler linkHandler;

    protected HostedDiscordPlatform(JDA jda, Platform parent, String prefix, DiscordLinkHandler linkHandler) {
        super(jda, prefix);
        this.parent = parent;
        this.linkHandler = linkHandler;
    }

    public Platform parent() {
        return parent;
    }

    @Override
    public Logger getLogger() {
        return parent.getLogger();
    }

    @Override
    public TaskScheduler getTaskScheduler() {
        return parent.getTaskScheduler();
    }

    @Override
    public SquirtgunUser getConsole() {
        return parent.getConsole();
    }

    @Override
    public DiscordUser getPlayer(UUID uuid) {
        String id = linkHandler.getDiscordId(uuid);
        if (id == null) {
            return null;
        }
        User user = jda().getUserById(id);
        if (user == null) {
            return null;
        }
        return audiences().user(user);
    }

    @Override
    public @Nullable DiscordUser getPlayer(String name) {
        SquirtgunPlayer player = parent().getPlayer(name);
        return player == null ? null : getPlayer(player.getUuid());
    }

    @Override
    public Path getConfigPath(SquirtgunPlugin<?> plugin) {
        return parent().getConfigPath(plugin);
    }
}
