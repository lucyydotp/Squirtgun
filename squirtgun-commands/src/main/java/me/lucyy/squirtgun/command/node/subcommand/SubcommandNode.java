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

package me.lucyy.squirtgun.command.node.subcommand;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A node that tab completes and acts as a "splitter" for other nodes.
 * As per the name, it can be used to create subcommands.
 *
 * @since 2.0.0
 */
public class SubcommandNode<T extends PermissionHolder> implements CommandNode<T> {

    @SafeVarargs
    public static <T extends PermissionHolder> SubcommandNode<T> withHelp(
            String name,
            @Nullable String permission,
            @NotNull CommandNode<T>... childNodes) {
        SubcommandNode<T> node = new SubcommandNode<>(name, permission, childNodes);
        node.setFallbackNode(new SubcommandHelpNode<>(node));
        return node;
    }

    @SafeVarargs
    public static <T extends PermissionHolder> SubcommandNode<T> withFallback(
            String name,
            @Nullable String permission,
            @NotNull CommandNode<T> fallback,
            @NotNull CommandNode<T>... childNodes) {
        SubcommandNode<T> node = new SubcommandNode<>(name, permission, childNodes);
        node.setFallbackNode(fallback);
        return node;
    }

    @SafeVarargs
    public static <T extends PermissionHolder> SubcommandNode<T> withBasicHelp(
            String name,
            @Nullable String permission,
            @NotNull CommandNode<T>... childNodes) {
        return new SubcommandNode<>(name, permission, childNodes);
    }

    private final Set<? extends CommandNode<T>> childNodes;
    private final String name;
    private final @Nullable String permission;
    private final CommandArgument<CommandNode<T>> argument;
    private CommandNode<T> fallbackNode;

    /**
     * @param name       this node's name.
     * @param permission the permission needed to execute this node. May be null if none is required.
     * @param childNodes the child nodes
     */
    @SafeVarargs
    protected SubcommandNode(@NotNull String name, @Nullable String permission, @NotNull CommandNode<T>... childNodes) {
        Preconditions.checkNotNull(childNodes, "Child nodes must not be null");
        Preconditions.checkNotNull(name, "Name must not be null");

        this.childNodes = new HashSet<>(Arrays.asList(childNodes));
        this.name = name;
        this.permission = permission;

        argument = new SubcommandNodeArgument<>(this, "subcommand", "The subcommand to execute");
    }

    /**
     * Gets the child nodes that this node holds.
     */
    public Set<? extends CommandNode<T>> getNodes() {
        return childNodes;
    }

    /**
     * Sets the fallback node.
     */
    private void setFallbackNode(CommandNode<T> fallback) {
        fallbackNode = fallback;
    }

    /**
     * Gets the fallback node.
     */
    public @Nullable CommandNode<T> getFallbackNode() {
        return fallbackNode;
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return ImmutableList.of(argument);
    }

    @Override
    public @Nullable CommandNode<T> next(CommandContext<T> context) {
        CommandNode<T> name = context.getArgumentValue(argument);
        return name == null ? fallbackNode : name;
    }

    @Override
    public @Nullable Component execute(CommandContext<T> context) {
        return null;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    } // TODO

    @Override
    public @Nullable String getPermission() {
        return permission;
    }
}