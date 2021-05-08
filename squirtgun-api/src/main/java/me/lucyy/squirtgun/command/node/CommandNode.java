package me.lucyy.squirtgun.command.node;

import com.sun.org.apache.xpath.internal.Arg;
import me.lucyy.squirtgun.command.argument.ArgumentChain;
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
	 * Appends this command's arguments to the chain.
	 *
	 * @param builder the builder to append arguments to
	 */
	@NotNull ArgumentChain.Builder<T> getArguments(ArgumentChain.Builder<T> builder);
}
