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
package net.lucypoulton.squirtgun.discord.adventure;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.kyori.adventure.audience.Audience;
import net.lucypoulton.squirtgun.discord.DiscordUser;
import org.jetbrains.annotations.NotNull;

/**
 * Obtains audiences from JDA entities.
 */
public interface DiscordAudiences {

    /**
     * Creates an audience for a Discord text channel.
     *
     * @param channel the channel to create an audience for
     * @return the audience
     */
    @NotNull Audience channel(TextChannel channel);

    /**
     * Creates an audience for a private message with a Discord account.
     * Note that users can disable private messaging from non-friends, in which case messages will not be received.
     *
     * @param user the user to open a private message with
     * @return the audience
     */
    @NotNull DiscordUser user(User user);
}
