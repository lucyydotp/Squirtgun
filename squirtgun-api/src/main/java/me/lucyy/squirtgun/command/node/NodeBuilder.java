package me.lucyy.squirtgun.command.node;

import com.google.common.base.Preconditions;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * A builder to build a node.
 *
 * @param <T>
 */
public class NodeBuilder<T> {

	private static class BuiltCommandNode<T> implements CommandNode<T> {

		private final String name;
		private final @Nullable String permission;
		private final Function<CommandContext<T>, @Nullable Component> executes;
		private final @Nullable CommandNode<T> next;
		private final List<CommandArgument<?>> arguments;

		private BuiltCommandNode(String name,
		                         @Nullable String permission,
		                         Function<CommandContext<T>, @Nullable Component> executes,
		                         @Nullable CommandNode<T> next,
		                         List<CommandArgument<?>> arguments) {
			this.name = name;
			this.permission = permission;
			this.executes = executes;
			this.next = next;
			this.arguments = arguments;
		}

		@Override
		public @Nullable Component execute(CommandContext<T> context) {
			return executes.apply(context);
		}

		@Override
		public @NotNull String getName() {
			return name;
		}

		@Override
		public @Nullable String getPermission() {
			return permission;
		}

		@Override
		public @NotNull List<CommandArgument<?>> getArguments() {
			return arguments;
		}

		@Override
		public @Nullable CommandNode<T> next(CommandContext<T> context) {
			return next;
		}
	}

	private String name;
	private String permission;
	private Function<CommandContext<T>, @Nullable Component> executes;
	private CommandNode<T> next;

	private final List<CommandArgument<?>> arguments = new ArrayList<>();

	/**
	 * Sets this node's name.
	 */
	public NodeBuilder<T> name(@NotNull String name) {
		Preconditions.checkNotNull(name, "Name must not be null");
		this.name = name;
		return this;
	}

	/**
	 * Sets this node's required permission.
	 *
	 * @param permission the required permission or null if none is needed
	 */
	public NodeBuilder<T> permission(@Nullable String permission) {
		this.permission = permission;
		return this;
	}

	/**
	 * Sets the function to execute this node.
	 *
	 * @param executes the function. It should return a component to display to the sender. This may be null, in which
	 *                 case nothing will be sent.
	 */
	public NodeBuilder<T> executes(@NotNull Function<CommandContext<T>, @Nullable Component> executes) {
		Preconditions.checkNotNull(executes, "Executes function must not be null");
		this.executes = executes;
		return this;
	}

	/**
	 * Sets the next node in the chain. Typically you don't need this, the default value of null will suffice.
	 *
	 * @param next a possibly null next node
	 */
	public NodeBuilder<T> next(@Nullable CommandNode<T> next) {
		this.next = next;
		return this;
	}

	/**
	 * Adds arguments to this node.
	 * @param arguments arguments to add, in order, to the list
	 */
	public NodeBuilder<T> arguments(@NotNull CommandArgument<?>... arguments) {
		this.arguments.addAll(Arrays.asList(arguments));
		return this;
	}

	/**
	 * Builds this node, throwing a {@link NullPointerException} if an argument is incorrect or missing.
	 *
	 * @return a node built from the specified parameters.
	 */
	public CommandNode<T> build() {
		return new BuiltCommandNode<>(name, permission, executes, next, arguments);
	}
}
