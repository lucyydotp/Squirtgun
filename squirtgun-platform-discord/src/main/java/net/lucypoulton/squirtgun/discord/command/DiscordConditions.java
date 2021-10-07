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
import net.dv8tion.jda.api.entities.Member;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.discord.DiscordUser;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;

import java.util.Arrays;

/**
 * Predefined conditions for Discord commands.
 */
public class DiscordConditions {

    public static Condition<PermissionHolder, DiscordUser> DISCORD_USER = (target, context) ->
        target instanceof DiscordUser ?
            new Condition.Result<>(true, (DiscordUser) target, null) :
            new Condition.Result<>(false, null, "This command can only be used from Discord");

    /**
     * Only allows human users - no bot accounts.
     */
    public static Condition<DiscordUser, DiscordUser> NO_BOTS = (target, ctx) ->
        new Condition.Result<>(!target.discordUser().isBot(), target, null);

    /**
     * Only allows the command to come from a specific set of channels.
     *
     * @param channelIds a list of channel IDs to accept commands from.
     */
    public static Condition<DiscordUser, DiscordUser> channels(long... channelIds) {
        return (target, context) ->
            new Condition.Result<>(Arrays.stream(channelIds)
                .anyMatch(id -> ((DiscordCommandContext) context).getMessage().getChannel().getIdLong() == id),
                target, null);
    }


    /**
     * Only allows the command to come from a specific set of guilds.
     *
     * @param guildIds a list of guild IDs to accept commands from.
     */
    public static Condition<DiscordUser, DiscordUser> guilds(long... guildIds) {
        return (target, context) ->
            new Condition.Result<>(Arrays.stream(guildIds)
                .anyMatch(id -> ((DiscordCommandContext) context).getMessage().getGuild().getIdLong() == id),
                target, null);
    }

    /**
     * Only allows the command to come from a sender with a specific Discord permission.
     *
     * @param permission the permission to check for
     */
    public static Condition<DiscordUser, DiscordUser> discordPermission(Permission permission) {
        return (target, context) -> {
            DiscordCommandContext discordCtx = (DiscordCommandContext) context;
            Member member = discordCtx.getMessage().getMember();
            return new Condition.Result<>(member == null || member.hasPermission(permission),
                target, null);
        };
    }
}
