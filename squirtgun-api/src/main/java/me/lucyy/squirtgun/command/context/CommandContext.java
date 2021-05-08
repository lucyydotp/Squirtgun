package me.lucyy.squirtgun.command.context;

import me.lucyy.squirtgun.command.FormatProvider;
import me.lucyy.squirtgun.command.argument.ArgumentChain;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A command context.
 *
 * @param <T>
 * @since 2.0.0
 */
public interface CommandContext<T> {

	/**
	 * Gets this command context's target.
	 */
	T getTarget();

	/**
	 * Gets the context's argument chain.
	 */
	@NotNull
	ArgumentChain<T> getArgumentChain();

	/**
	 * Gets the value of an argument.
	 *
	 * @param argument the argument to get the value for
	 * @return the value if available - if unset then null
	 */
	@Nullable
	<U> U getArgumentValue(CommandArgument<U> argument);

	/**
	 * Gets the format provider to use in message decoration.
	 */
	@NotNull FormatProvider getFormat();
}
