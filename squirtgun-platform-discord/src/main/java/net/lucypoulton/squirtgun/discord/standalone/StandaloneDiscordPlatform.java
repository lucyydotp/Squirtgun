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

import net.dv8tion.jda.api.JDA;
import net.lucypoulton.squirtgun.discord.DiscordPlatform;
import net.lucypoulton.squirtgun.discord.DiscordUser;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunUser;
import net.lucypoulton.squirtgun.platform.scheduler.TaskScheduler;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.logging.Logger;

public class StandaloneDiscordPlatform extends DiscordPlatform {

    private final TaskScheduler scheduler = new StandaloneTaskScheduler(this);
    private final SquirtgunUser console = new StandaloneConsoleWrapper(this);

    public StandaloneDiscordPlatform(JDA jda) {
        super(jda);
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }

    /**
     * Throws an {@link IllegalStateException} - this method is <b>not supported standalone</b>
     */
    @Override
    public DiscordUser getPlayer(UUID uuid) {
        throw new IllegalStateException("Standalone");
    }

    /**
     * Throws an {@link IllegalStateException} - this method is <b>not supported standalone</b>
     */
    @Override
    public @Nullable DiscordUser getPlayer(String name) {
        throw new IllegalStateException("Standalone");
    }

    @Override
    public Logger getLogger() {
        // fixme - is this right?
        return Logger.getGlobal();
    }

    @Override
    public TaskScheduler getTaskScheduler() {
        return scheduler;
    }

    @Override
    public SquirtgunUser getConsole() {
        return console;
    }
}
