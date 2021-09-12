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

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.kyori.adventure.audience.Audience;
import net.lucypoulton.squirtgun.discord.DiscordUser;
import net.lucypoulton.squirtgun.discord.adventure.ChannelAudience;
import net.lucypoulton.squirtgun.discord.adventure.DiscordAudiences;
import net.lucypoulton.squirtgun.discord.standalone.StandaloneDiscordUser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HostedDiscordAudiences implements DiscordAudiences {

    private final DiscordLinkHandler linkHandler;
    private final HostedDiscordPlatform platform;

    public HostedDiscordAudiences(DiscordLinkHandler linkHandler, HostedDiscordPlatform platform) {
        this.linkHandler = linkHandler;
        this.platform = platform;
    }

    @Override
    public @NotNull Audience channel(TextChannel channel) {
        return new ChannelAudience(channel);
    }

    @Override
    public @NotNull DiscordUser user(User user) {
        UUID mcUuid = linkHandler.getMinecraftUuid(user.getId());
        if (mcUuid == null) return new StandaloneDiscordUser(user);

        return new HostedDiscordUser(platform.parent().getPlayer(mcUuid), user);
    }
}
