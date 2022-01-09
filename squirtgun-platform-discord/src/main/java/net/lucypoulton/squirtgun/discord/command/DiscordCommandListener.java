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

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.discord.DiscordPlatform;
import net.lucypoulton.squirtgun.format.FormatProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DiscordCommandListener extends ListenerAdapter {

    private final DiscordPlatform platform;
    private final String prefix;

    private final Map<String, CommandNode<?>> nodes = new HashMap<>();
    private final Map<String, FormatProvider> formatProviders = new HashMap<>();

    public DiscordCommandListener(DiscordPlatform platform, String prefix) {
        this.platform = platform;
        this.prefix = prefix;
        platform.jda().addEventListener(this);
    }

    /**
     * Registers a command, overwriting any nodes with the same name.
     *
     * @param node           the command node to register
     * @param formatProvider the format provider to use for this node
     */
    public void registerCommand(CommandNode<?> node, FormatProvider formatProvider) {
        nodes.put(node.getName(), node);
        formatProviders.put(node.getName(), formatProvider);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().startsWith(prefix)) {
            return;
        }

        String cmdRaw = event.getMessage().getContentRaw().substring(prefix.length());
        String[] parts = cmdRaw.split(" ", 2);
        CommandNode<?> node = nodes.get(parts[0]);
        if (node == null) {
            return;
        }
        Component ret = new DiscordCommandContext(formatProviders.get(node.getName()),
            platform.audiences().user(event.getAuthor()),
            node, parts.length == 2 ? parts[1] : "",
            event.getMessage())
            .execute();

        if (ret != null) {
            platform.audiences().channel(event.getTextChannel()).sendMessage(ret);
        }
    }
}
