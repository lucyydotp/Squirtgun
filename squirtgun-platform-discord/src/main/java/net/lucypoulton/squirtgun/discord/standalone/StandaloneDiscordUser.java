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

import net.dv8tion.jda.api.entities.User;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.discord.DiscordUser;
import net.lucypoulton.squirtgun.discord.adventure.DiscordComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class StandaloneDiscordUser implements DiscordUser {

    private final User user;

    public StandaloneDiscordUser(User user) {
        this.user = user;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false; // TODO
    }

    @Override
    public void sendMessage(final @NotNull Identity source,
                            final @NotNull Component component,
                            final @NotNull MessageType type) {
        user.openPrivateChannel()
                .queue(channel -> channel.sendMessage(
                        DiscordComponentSerializer.INSTANCE.serialize(component)).queue()
                );
    }
}
