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

package net.lucypoulton.squirtgun.command.node;

import net.lucypoulton.squirtgun.command.PermissionHolder;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.format.node.TextNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A command node that shows a help screen for another node
 * that shows its description, usage, and argument information.
 *
 * @since 2.0.0
 */
public class HelpNode implements CommandNode<PermissionHolder> {

    private final CommandNode<?> parentNode;

    public HelpNode(CommandNode<?> parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public TextNode execute(CommandContext context) {
        FormatProvider format = context.getFormat();
        List<TextNode> nodes = new ArrayList<>();
        nodes.add(format.title("Command Help\n"));

        String fullCommand = parentNode.getName() + " " +
                parentNode.getArguments().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(" "));

        nodes.add(format.title("Command Help"));
        nodes.add(format.subtitle(parentNode.getDescription()));
        nodes.add(format.accent("Usage"));
        nodes.add(format.main(": " + fullCommand + "\n"));


        if (parentNode.getArguments().size() != 0) {
            nodes.add(format.subtitle("Arguments"));
            for (CommandArgument<?> argument : parentNode.getArguments()) {
                nodes.add(format.accent(argument.getName()));
                nodes.add(format.main(" - " + argument.getDescription() + "\n"));
            }
        }

        return TextNode.ofChildren(nodes.toArray(new TextNode[0]));
    }

    @Override
    public @NotNull String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows help on using this command.";
    }

    @Override
    public @NotNull Condition<PermissionHolder, ? extends PermissionHolder> getCondition() {
        return Condition.alwaysTrue();
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return Collections.emptyList();
    }
}
