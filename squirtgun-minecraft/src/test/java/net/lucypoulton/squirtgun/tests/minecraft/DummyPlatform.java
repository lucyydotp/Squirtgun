package net.lucypoulton.squirtgun.tests.minecraft;

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

import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.minecraft.platform.AuthMode;
import net.lucypoulton.squirtgun.minecraft.platform.Platform;
import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunUser;
import net.lucypoulton.squirtgun.minecraft.platform.event.EventManager;
import net.lucypoulton.squirtgun.minecraft.platform.scheduler.TaskScheduler;
import net.lucypoulton.squirtgun.minecraft.plugin.SquirtgunPlugin;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class DummyPlatform implements Platform {


    @Override
    public String name() {
        return "dummy";
    }

    @Override
    public Logger getLogger() {
        return Logger.getAnonymousLogger();
    }


    @Override
    public void log(Component component) {

    }

    @Override
    public AuthMode getAuthMode() {
        return AuthMode.ONLINE;
    }

    @Override
    public TaskScheduler getTaskScheduler() {
        return null;
    }

    @Override
    public EventManager getEventManager() {
        return null;
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
}
