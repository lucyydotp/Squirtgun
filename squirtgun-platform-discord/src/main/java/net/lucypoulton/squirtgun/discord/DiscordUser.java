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
package net.lucypoulton.squirtgun.discord;

import net.dv8tion.jda.api.entities.User;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.discord.adventure.DiscordComponentSerializer;
import net.lucypoulton.squirtgun.platform.Gamemode;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordUser implements SquirtgunPlayer {
    public abstract User discordUser();

    @Override
    public String getUsername() {
        return discordUser().getName();
    }

    /**
     * Not supported by Discord - always returns false
     *
     * @return false
     */
    @Override
    public boolean isOnline() {
        return false;
    }

    /**
     * Sends a direct message to a user.
     */
    @Override
    public void sendMessage(final @NotNull Identity source,
                            final @NotNull Component component,
                            final @NotNull MessageType type) {
        discordUser().openPrivateChannel()
            .queue(channel -> channel.sendMessage(DiscordComponentSerializer.INSTANCE.serialize(component)).queue());
    }

    /**
     * Discord obviously doesn't have gamemodes, returns a special value
     *
     * @return a unique Gamemode specific to Discord
     */
    @Override
    public Gamemode getGamemode() {
        return Gamemode.valueOf("DISCORD");
    }

    /**
     * Does nothing - Discord doesn't have gamemodes.
     */
    @Override
    public void setGamemode(Gamemode mode) {

    }


}
