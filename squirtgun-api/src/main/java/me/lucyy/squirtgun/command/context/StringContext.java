package me.lucyy.squirtgun.command.context;

import me.lucyy.squirtgun.command.FormatProvider;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

/**
 * This needs some serious optimisation and is only intended as a POC
 */
public class StringContext<T> implements CommandContext<T> {

	private final FormatProvider provider;
	private final T target;
	private List<CommandArgument<?>> arguments;
	private final Map<CommandArgument<?>, Object> argValues = new LinkedHashMap<>();
	private final CommandNode<T> node;

	public StringContext(FormatProvider provider, T target, CommandNode<T> node, String value) {
		this.provider = provider;
		this.target = target;
		this.node = node;

		Queue<String> argsSet = new LinkedList<>(Arrays.asList(value.split(" ")));

		this.arguments = node.getArguments(this);
		for (CommandArgument<?> arg : arguments) {
			argValues.put(arg, arg.getValue(argsSet));
		}
	}

	@Override
	public T getTarget() {
		return target;
	}

	@Override
	public @Nullable CommandArgument<?> getHeadArgument() {
		arguments = node.getArguments(this);
		if (arguments.size() == 0) return null;
		return arguments.get(arguments.size() - 1);
	}

	@Override
	public @Nullable String getArgumentValue(String name) {
		return argValues.get(name);
	}

	@Override
	public @NotNull FormatProvider getFormat() {
		return provider;
	}
}
