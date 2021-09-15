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
package net.lucypoulton.squirtgun.discord.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Predefined predicates for Discord commands.
 */
public class CommandPredicate {
    /**
     * Only allows human users - no bot accounts.
     */
    public static Predicate<Message> NO_BOTS = msg -> !msg.getAuthor().isBot();

    /**
     * Only allows the command to come from a specific set of channels.
     *
     * @param channelIds a list of channel IDs to accept commands from.
     */
    public static Predicate<Message> channels(long... channelIds) {
        return msg -> Arrays.stream(channelIds).anyMatch(id -> msg.getChannel().getIdLong() == id);
    }

    /**
     * Only allows the command to come from a specific set of guilds.
     *
     * @param guildIds a list of guild IDs to accept commands from.
     */
    public static Predicate<Message> guilds(long... guildIds) {
        return msg -> msg.isFromGuild() && Arrays.stream(guildIds).anyMatch(id -> msg.getGuild().getIdLong() == id);
    }

    /**
     * Only allows the command to come from a sender with a specific Discord permission.
     *
     * @param permission the permission to check for
     */
    public static Predicate<Message> discordPermission(Permission permission) {
        return msg -> !msg.isFromGuild() || Objects.requireNonNull(msg.getMember()).hasPermission(permission);
    }
}
