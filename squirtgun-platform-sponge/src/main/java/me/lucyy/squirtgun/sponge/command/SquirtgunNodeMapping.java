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
package me.lucyy.squirtgun.sponge.command;

import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import org.spongepowered.api.command.manager.CommandMapping;
import org.spongepowered.api.command.registrar.CommandRegistrar;
import org.spongepowered.plugin.PluginContainer;

import java.util.Collections;
import java.util.Set;

public class SquirtgunNodeMapping<T extends PermissionHolder> implements CommandMapping {

    private final PluginContainer pluginContainer;
    private final CommandNode<T> node;
    private final CommandRegistrar<?> registrar;

    public SquirtgunNodeMapping(PluginContainer pluginContainer, CommandNode<T> node, CommandRegistrar<?> registrar) {
        this.pluginContainer = pluginContainer;
        this.node = node;
        this.registrar = registrar;
    }

    @Override
    public String primaryAlias() {
        return node.getName();
    }

    @Override
    public Set<String> allAliases() {
        return Collections.emptySet();
    }

    @Override
    public PluginContainer plugin() {
        return pluginContainer;
    }

    @Override
    public CommandRegistrar<?> registrar() {
        return registrar;
    }

    public CommandNode<T> node() {
        return node;
    }
}
