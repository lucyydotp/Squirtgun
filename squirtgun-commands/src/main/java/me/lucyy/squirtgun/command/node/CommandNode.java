package me.lucyy.squirtgun.command.node;

import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * A node as part of a command chain.
 * @param <T> the type of object to pass in command context
 *
 * @since 2.0.0
 */
public interface CommandNode<T extends PermissionHolder> {

	/**
	 * Execute this node. This will only be executed if there are no further nodes
	 * in the chain.
	 *
	 * @param context the context that this command was executed in
	 * @return a component to optionally 
	 */
	@Nullable Component execute(CommandContext<T> context);

	/**
	 * Gets this node's name, which will be used as a literal if needed.
	 */
	@NotNull String getName();

	/**
	 * Gets this node's description for use in help commands. This should be a simple,
	 * one-line sentence.
	 */
	String getDescription();

	/**
	 * Gets the permission needed to execute this node or any children.
	 *
	 * @return the permission, or null if no permission is needed
	 */
	default @Nullable String getPermission() {
		return null;
	}

	/**
	 * Gets this command's arguments. By default, returns an empty list.
	 */
	default @NotNull List<CommandArgument<?>> getArguments() {
		return Collections.emptyList();
	}

	/**
	 * Returns the node following this node.
	 *
	 * @return the next node in the chain, or if this is the end of the chain, null
	 */
	default @Nullable CommandNode<T> next(CommandContext<T> context) {
		return null;
	}
}
