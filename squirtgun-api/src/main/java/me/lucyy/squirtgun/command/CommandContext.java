package me.lucyy.squirtgun.command;

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
	 * Gets the head argument.
	 *
	 * @return the last argument. If this command is currently being tabcompleted, returns the argument the user is
	 * currently typing out, or if the user is not tabcompleting a valid argument, null.
	 */
	@Nullable CommandArgument<?> getHeadArgument();

	/**
	 * Gets an argument's value.
	 *
	 * @param argument the argument to get the value for
	 * @return the value if it's been specified, otherwise null
	 */
	@Nullable <U> U getArgumentValue(CommandArgument<U> argument);

	/**
	 * Gets the format provider to use in message decoration.
	 */
	@NotNull FormatProvider getFormat();
}
