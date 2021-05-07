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
	 * Gets the command's arguments, in order.
	 *
	 * @param context the command context - this may be ignored if not needed
	 */
	@NotNull List<CommandArgument<?>> getArguments(CommandContext<T> context);
}
