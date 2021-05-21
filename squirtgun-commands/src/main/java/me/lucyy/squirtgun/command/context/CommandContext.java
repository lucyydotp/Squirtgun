/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lucyy.squirtgun.command.context;

import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * The context a command is executed in, responsible for parsin, tab-completing
 * and executing the command. It contains data such as the command target and
 * the arguments provided to it so far.
 *
 * @param <T> the subject type.
 * @since 2.0.0
 */
public interface CommandContext<T extends PermissionHolder> {

	/**
	 * Gets this command context's target.
	 */
	T getTarget();

	/**
	 * Gets the value of an argument.
	 *
	 * @param argument the argument to get the value for
	 * @return the value if available - if unset then null
	 */
	@Nullable
	<U> U getArgumentValue(CommandArgument<U> argument);

	/**
	 * Gets the value of an argument by name.
	 *
	 * @param name the name of the argument to get the value for
	 * @return the value if available, if unset then null.
	 * @throws IllegalArgumentException if the name given is not a registered argument
	 */
	@Nullable
	Object getArgumentValue(String name);

	/**
	 * Gets the raw input given to this context.
	 */
	String getRaw();

	/**
	 * Gets the format provider to use in message decoration.
	 */
	@NotNull
	FormatProvider getFormat();

	/**
	 * Gets the string to be shown in the tabcompleter.
	 */
	@Nullable List<String> tabComplete();

	/**
	 * Executes the command.
	 */
	Component execute();

	/**
	 * Gets the last node in the chain that has been reached.
	 */
	CommandNode<T> getTail();
}
