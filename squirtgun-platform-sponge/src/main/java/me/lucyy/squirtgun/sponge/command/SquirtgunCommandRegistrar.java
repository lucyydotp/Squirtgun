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
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.manager.CommandFailedRegistrationException;
import org.spongepowered.api.command.manager.CommandMapping;
import org.spongepowered.api.command.registrar.CommandRegistrar;
import org.spongepowered.api.command.registrar.CommandRegistrarType;
import org.spongepowered.plugin.PluginContainer;

import java.util.List;
import java.util.Optional;

public class SquirtgunCommandRegistrar<T extends PermissionHolder> implements CommandRegistrar<CommandNode<T>> {

    private final SquirtgunRegistrarType<T> type = new SquirtgunRegistrarType<>();

    @Override
    public CommandRegistrarType<CommandNode<T>> type() {
        return type;
    }

    @Override
    public CommandMapping register(PluginContainer container, CommandNode<T> command, String primaryAlias, String... secondaryAliases) throws CommandFailedRegistrationException {
        return null;
    }

    @Override
    public CommandResult process(CommandCause cause, CommandMapping mapping, String command, String arguments) throws CommandException {
        return null;
    }

    @Override
    public List<CommandCompletion> complete(CommandCause cause, CommandMapping mapping, String command, String arguments) throws CommandException {
        return null;
    }

    @Override
    public Optional<Component> help(CommandCause cause, CommandMapping mapping) {
        ((SquirtgunNodeMapping<T>) mapping).node();
        return Optional.empty();
    }

    @Override
    public boolean canExecute(CommandCause cause, CommandMapping mapping) {
        return false;
    }
}
