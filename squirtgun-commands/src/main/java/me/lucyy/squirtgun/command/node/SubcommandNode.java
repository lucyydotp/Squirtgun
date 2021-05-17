package me.lucyy.squirtgun.command.node;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.argument.ListArgument;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A node that tab completes and acts as a splitter for other nodes.
 * TODO
 */
public class SubcommandNode<T extends PermissionHolder> implements CommandNode<T> {

	private final Set<? extends CommandNode<T>> childNodes;
	private final String name;
	private final @Nullable String permission;
	private final CommandArgument<String> argument;

	@SafeVarargs
	public SubcommandNode(@NotNull String name, @Nullable String permission,
	                      boolean addHelpNode, CommandNode<T>... childNodes) {
		Preconditions.checkNotNull(childNodes, "Child nodes must not be null");
		Preconditions.checkNotNull(name, "Name must not be null");

		Set<CommandNode<T>> nodes = new HashSet<>(Arrays.asList(childNodes));
		if (addHelpNode) {
			nodes.add(new SubcommandHelpNode<>(this));
		}

		this.childNodes = nodes;
		this.name = name;
		this.permission = permission;
		argument = new ListArgument("subcommand",
				"The subcommand to execute",
				true,
				this.childNodes.stream().map(CommandNode::getName).collect(Collectors.toList()));
	}

	private @Nullable CommandNode<T> getNode(String name) {
		return childNodes.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
	}

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return ImmutableList.of(argument);
	}

	public Set<? extends CommandNode<T>> getNodes() {
		return childNodes;
	}

	@Override
	public @Nullable CommandNode<T> next(CommandContext<T> context) {
		String name = context.getArgumentValue(argument);
		return name == null ? null : getNode(name);
	}

	@Override
	public @Nullable Component execute(CommandContext<T> context) {
		FormatProvider format = context.getFormat();
		Component out = Component.empty()
				.append(TextFormatter.formatTitle("Command Help", format))
				.append(Component.newline());

		for (CommandNode<?> node : childNodes) {
			out = out.append(format.formatAccent(node.getName()))
					.append(format.formatMain(" - " + node.getDescription()))
					.append(Component.newline());
		}

		out = out.append(TextFormatter.formatTitle("*", context.getFormat()));

		return out;
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
