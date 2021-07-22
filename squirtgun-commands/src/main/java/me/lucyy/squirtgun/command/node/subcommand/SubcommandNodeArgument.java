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

import me.lucyy.squirtgun.command.argument.AbstractArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubcommandNodeArgument<T extends PermissionHolder> extends AbstractArgument<CommandNode<T>> {

	private final SubcommandNode<T> parent;

	/**
	 * @param parent      the node this argument belongs to
	 * @param name        the argument's name
	 * @param description the argument's description
	 */
	public SubcommandNodeArgument(SubcommandNode<T> parent, String name, String description) {
		super(name, description, false);
		this.parent = parent;
	}

	private Stream<? extends CommandNode<T>> getValidNodes(String name, PermissionHolder holder) {
		return parent.getNodes().stream()
				.filter(node -> node.getName().toLowerCase(Locale.ROOT).startsWith(name.toLowerCase(Locale.ROOT)))
				.filter(node -> node.getCondition().test(holder));
	}

	@Override
	public CommandNode<T> getValue(Queue<String> args, CommandContext<?> context) {
		String raw = args.poll();
		if (raw == null || raw.equals("")) return null;
		return getValidNodes(raw, context.getTarget())
				.min(Comparator.comparingInt(a -> a.getName().length()))
				.orElse(null);
	}

	@Override
	public @Nullable List<String> tabComplete(Queue<String> args, CommandContext<?> context) {
		String raw = args.poll();
		if (raw == null) return null;
		return getValidNodes(raw, context.getTarget())
				.map(CommandNode::getName)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isOptional() {
		return parent.getFallbackNode() == null;
	}
}
