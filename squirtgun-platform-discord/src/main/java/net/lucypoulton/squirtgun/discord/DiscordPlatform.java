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
package net.lucypoulton.squirtgun.discord;import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.discord.adventure.DiscordAudiences;
import net.lucypoulton.squirtgun.discord.adventure.DiscordComponentSerializer;
import net.lucypoulton.squirtgun.discord.standalone.StandaloneDiscordAudiences;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.AuthMode;
import net.lucypoulton.squirtgun.platform.EventListener;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunUser;
import net.lucypoulton.squirtgun.plugin.SquirtgunPlugin;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * A Platform implementation for a JDA instance.
 * TODO
 */
public abstract class DiscordPlatform implements Platform {

    private final JDA jda;
    private final DiscordAudiences audiences;

    protected JDA jda() {
        return jda;
    }

    protected DiscordPlatform(JDA jda) {
        this.jda = jda;
        audiences = new StandaloneDiscordAudiences(jda);
    }

    @Override
    public String name() {
        return "Discord";
    }

    /**
     * Authentication mode is not relevant to Discord so always returns offline
     * @return {@link AuthMode#OFFLINE}
     */
    @Override
    public AuthMode getAuthMode() {
        return AuthMode.OFFLINE;
    }

    /**
     * This method has not been implemented, events will not be triggered. See issue #40.
     */
    @Override
    public void registerEventListener(EventListener listener) {
    }

    /**
     * This method has not been implemented, events will not be triggered. See issue #40.
     */
    @Override
    public void unregisterEventListener(EventListener listener) {
    }

    @Override
    public SquirtgunUser getConsole() {
        return null;
    }

    @Override
    public SquirtgunPlayer getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public @Nullable SquirtgunPlayer getPlayer(String name) {
        return null;
    }

    @Override
    public List<SquirtgunPlayer> getOnlinePlayers() {
        return null;
    }

    @Override
    public Path getConfigPath(SquirtgunPlugin<?> plugin) {
        return null;
    }

    @Override
    public void registerCommand(CommandNode<?> node, FormatProvider provider) {

    }

    @Override
    public void log(Component component) {
        getLogger().info(DiscordComponentSerializer.INSTANCE.serialize(component));
    }

    public DiscordAudiences audiences() {
        return audiences;
    }
}
