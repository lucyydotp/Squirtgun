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
package net.lucypoulton.squirtgun.discord.discordsrv;

import github.scarsz.discordsrv.DiscordSRV;
import net.dv8tion.jda.api.JDA;
import net.lucypoulton.squirtgun.discord.hosted.HostedDiscordPlatform;
import net.lucypoulton.squirtgun.minecraft.platform.Platform;
import org.jetbrains.annotations.Nullable;

public class DiscordSrvSquirtgun {

    public static final boolean discordSrvPresent;

    static {
        boolean pluginPresent;
        try {
            Class.forName("github.scarsz.discordsrv.DiscordSRV");
            pluginPresent = true;
        } catch (ClassNotFoundException e) {
            pluginPresent = false;
        }
        discordSrvPresent = pluginPresent;
    }

    /**
     * Creates a new HostedDiscordPlatform using DiscordSRV for account linking.
     *
     * @param jda            the JDA instance to use. DiscordSRV relocates JDA so reusing its one isn't an option sadly
     * @param parentPlatform the parent platform
     * @param prefix         the prefix to use for commands
     * @return a discord platform, or null if DiscordSRV is not present
     */
    public static @Nullable HostedDiscordPlatform create(JDA jda, Platform parentPlatform, String prefix) {
        if (!discordSrvPresent) {
            parentPlatform.getLogger().severe("DiscordSRV is not present!");
            return null;
        }
        return new HostedDiscordPlatform(jda,
            parentPlatform, prefix,
            new DiscordSrvLinkHandler(DiscordSRV.getPlugin().getAccountLinkManager())
        );
    }
}
