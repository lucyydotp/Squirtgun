package me.lucyy.squirtgun.command.node;

import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * A node as part of a command chain.
 * @param <T> the type of object to pass in command context
 *
 * @since 2.0.0
 */
public interface CommandNode<T> {

	/**
	 * Execute this node.
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
	 * Gets the permission needed to execute this node.
	 *
	 * @return the permission, or null if no permission is needed
	 */
	@Nullable String getPermission();


	/**
	 * Gets this command's arguments.
	 */
	@NotNull List<CommandArgument<?>> getArguments();

	/**
	 * Returns the node following this node.
	 *
	 * @return the next node in the chain, or if this is the end of the chain, null
	 */
	default @Nullable CommandNode<T> next(CommandContext<T> context) {
		return null;
	}
}
