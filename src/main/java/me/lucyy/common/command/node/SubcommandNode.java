package me.lucyy.common.command.node;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.sun.org.apache.xpath.internal.Arg;
import me.lucyy.common.command.CommandContext;
import me.lucyy.common.command.argument.AbstractArgument;
import me.lucyy.common.command.argument.CommandArgument;
import me.lucyy.common.command.argument.ListArgument;
import me.lucyy.common.command.argument.SingleWordArgument;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A node that tab completes and acts as a splitter for other nodes.
 * TODO
 */
public class SubcommandNode<T> implements CommandNode<T> {

	private final Set<CommandNode<T>> childNodes;
	private final String name;
	private final @Nullable String permission;
	private final CommandArgument<String> argument;

	public SubcommandNode(@NotNull Set<CommandNode<T>> childNodes, @NotNull String name, @Nullable String permission) {
		Preconditions.checkNotNull(childNodes, "Child nodes must not be null");
		Preconditions.checkNotNull(name, "Name must not be null");

		this.childNodes = childNodes;
		this.name = name;
		this.permission = permission;
		argument = new ListArgument("subcommand",
				"The subcommand to execute",
				childNodes.stream().map(CommandNode::getName).collect(Collectors.toList()));
	}

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return ImmutableList.of(argument); // TODO - implement CommandArgument to forward onto the next command
	}

	private Component helpMessage() {
		return Component.text("help message"); // TODO
	}

	@Override
	public @Nullable Component execute(CommandContext<T> context) {
		String subcommand = context.getArgumentValue(argument);
		Preconditions.checkNotNull(subcommand);
		Optional<CommandNode<T>> node = childNodes.stream().filter(x -> x.getName().equals(subcommand)).findFirst();

		if (!node.isPresent()) return helpMessage();

		return node.get().execute(context);
	}

	@Override
	public @NotNull String getName() {
		return name;
	}

	@Override
	public @Nullable String getPermission() {
		return permission;
	}
}
