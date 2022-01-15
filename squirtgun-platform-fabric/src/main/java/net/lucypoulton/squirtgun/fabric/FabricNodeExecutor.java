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

package net.lucypoulton.squirtgun.fabric;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.command.context.StringContext;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.minecraft.platform.audience.SquirtgunUser;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Command adapter between Fabric and Squirtgun command execution.
 */
public class FabricNodeExecutor implements Command<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {

    private final CommandNode<?> commandNode;
    private final FormatProvider formatProvider;
    private final FabricPlatform platform;

    public FabricNodeExecutor(
            final @NotNull CommandNode<?> commandNode,
            final @NotNull FormatProvider formatProvider,
            final @NotNull FabricPlatform platform
    ) {
        this.commandNode = Objects.requireNonNull(commandNode, "commandNode");
        this.formatProvider = Objects.requireNonNull(formatProvider, "formatProvider");
        this.platform = Objects.requireNonNull(platform, "platform");
    }

    @Override
    public int run(final CommandContext<ServerCommandSource> context) {
        String input = context.getInput();
        final int i = input.indexOf(' ');
        input = i > -1 ? input.substring(i + 1) : "";

        final SquirtgunUser source = this.platform.fromCommandSource(context.getSource());
        final Component result = new StringContext(this.formatProvider, source, this.commandNode, input).execute();
        if (result != null) {
            source.sendMessage(result);
        }

        return Command.SINGLE_SUCCESS;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(final CommandContext<ServerCommandSource> context, final SuggestionsBuilder builder) {
        String input = context.getInput();
        final int i = input.indexOf(' ');
        input = i > -1 ? input.substring(i + 1) : "";

        final SquirtgunUser source = this.platform.fromCommandSource(context.getSource());
        final List<String> suggestions = new StringContext(this.formatProvider, source, this.commandNode, input).tabComplete();
        if (suggestions == null) {
            return Suggestions.empty();
        } else {
            suggestions.forEach(builder::suggest);
            return builder.buildFuture();
        }
    }
}
