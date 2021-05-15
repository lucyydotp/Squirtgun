package me.lucyy.squirtgun.command.node;

import com.google.common.base.Preconditions;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * A builder to build a node.
 *
 * @param <T> the type to expect from the command context
 */
public class NodeBuilder<T extends PermissionHolder> {

	private static class BuiltCommandNode<T extends PermissionHolder> implements CommandNode<T> {

		private final String name;
		private final String description;
		private final @Nullable String permission;
		private final Function<CommandContext<T>, @Nullable Component> executes;
		private final @Nullable CommandNode<T> next;
		private final List<CommandArgument<?>> arguments;

		private BuiltCommandNode(String name,
								 String description,
								 @Nullable String permission,
								 Function<CommandContext<T>, @Nullable Component> executes,
								 @Nullable CommandNode<T> next,
								 List<CommandArgument<?>> arguments) {
			this.name = name;
			this.description = description;
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
		public String getDescription() {
			return description;
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
	private String description;
	private String permission;
	private Function<CommandContext<T>, @Nullable Component> executes;
	private CommandNode<T> next;

	private final List<CommandArgument<?>> arguments = new ArrayList<>();

	/**
	 * Sets this node's name.
	 *
	 * @param name the new name to set
	 * @return this
	 */
	public NodeBuilder<T> name(@NotNull String name) {
		Preconditions.checkNotNull(name, "Name must not be null");
		this.name = name;
		return this;
	}

	/**
	 * Sets this node's description.
	 *
	 * @param description the new description to set - this should be a short, 1-line sentence
	 * @return this
	 */
	public NodeBuilder<T> description(@NotNull String description) {
		this.description = description;
		return this;
	}

	/**
	 * Sets this node's required permission.
	 *
	 * @param permission the required permission or null if none is needed
	 * @return this
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
	 * @return this
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
	 * @return this
	 */
	public NodeBuilder<T> next(@Nullable CommandNode<T> next) {
		this.next = next;
		return this;
	}

	/**
	 * Adds arguments to this node.
	 *
	 * @param arguments arguments to add, in order, to the list
	 * @return this
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
		return new BuiltCommandNode<>(name, description, permission, executes, next, arguments);
	}
}
