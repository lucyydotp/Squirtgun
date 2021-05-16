package me.lucyy.squirtgun.command.argument;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Queue;

/**
 * An argument.
 */
public interface CommandArgument<T> {

	/**
	 * Gets this argument's name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Gets this argument's description for use in help commands. This should be a simple, one-line sentence.
	 *
	 * @return the description
	 */
	String getDescription();

	/**
	 * Reads from a queue to get the value for this argument.
	 *
	 * @param args a queue of strings containing the raw arguments. Pop as many as needed and no more.
	 * @return the parsed string value of this argument.
	 */
	T getValue(Queue<String> args);

	/**
	 * Tab-completes this node.
	 *
	 * @param args a queue of strings containing the raw arguments. Pop as many as needed and no more.
	 * @return the tabcompleted value of this node, or if not applicable, null
	 */
	@Nullable List<String> tabComplete(Queue<String> args);

	/**
	 * Gets whether this argument is optional or not. This should not change how the argument behaves.
	 */
	boolean isOptional();
}
