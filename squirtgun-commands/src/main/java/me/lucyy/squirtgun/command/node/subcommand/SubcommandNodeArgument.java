package me.lucyy.squirtgun.command.node.subcommand;

import me.lucyy.squirtgun.command.argument.AbstractArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.PermissionHolder;
import org.jetbrains.annotations.Nullable;
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
	 * @param isOptional  whether the argument is optional or not
	 */
	public SubcommandNodeArgument(SubcommandNode<T> parent, String name, String description, boolean isOptional) {
		super(name, description, isOptional);
		this.parent = parent;
	}

	private Stream<? extends CommandNode<T>> getValidNodes(String name, PermissionHolder holder) {
		return parent.getNodes().stream()
				.filter(node -> node.getName().toLowerCase(Locale.ROOT).startsWith(name.toLowerCase(Locale.ROOT)))
				.filter(node -> node.getPermission() == null || holder.hasPermission(node.getPermission()));
	}

	@Override
	public CommandNode<T> getValue(Queue<String> args, CommandContext<?> context) {
		String raw = args.poll();
		if (raw == null) return null;
		return getValidNodes(raw, context.getTarget())
				.findFirst()
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
}
