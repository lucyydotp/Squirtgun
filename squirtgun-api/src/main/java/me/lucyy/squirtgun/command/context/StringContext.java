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
	private final String raw;
	private final LinkedHashMap<CommandArgument<?>, Object> argValues = new LinkedHashMap<>();
	private final CommandNode<T> root;

	private void populateArguments(CommandNode<T> node, Queue<String> raw) {
		for (CommandArgument<?> arg : node.getArguments()) {
			argValues.put(arg, arg.getValue(raw));
		}
		CommandNode<T> next = node.next(this);
		if (next == null) return;

		populateArguments(next, raw);
	}

	private List<String> tabCompleteNode(CommandNode<T> node, Queue<String> raw) {
		@Nullable List<String> result = null;
		for (CommandArgument<?> arg : node.getArguments()) {
			result = arg.tabComplete(raw);
			if (result == null) return null;
		}

		if (result == null) return null;

		CommandNode<T> next = node.next(this);
		if (next != null) return tabCompleteNode(next, raw);

		if (!raw.isEmpty()) return null;
			return result;
	}

	private Queue<String> getArgsAsList(String value) {
		return new LinkedList<>(Arrays.asList(value.split(" ")));
	}

	public StringContext(FormatProvider provider, T target, CommandNode<T> node, String value) {
		this.raw = value;
		this.provider = provider;
		this.root = node;
		this.target = target;

		populateArguments(node, getArgsAsList(value));
	}

	@Override
	public T getTarget() {
		return target;
	}


	@Override
	@SuppressWarnings("unchecked") // safety is ensured by logic
	public @Nullable <U> U getArgumentValue(CommandArgument<U> name) {
		return (U) argValues.get(name);
	}

	@Override
	public @NotNull FormatProvider getFormat() {
		return provider;
	}

	@Override
	public List<String> tabComplete() {
		return tabCompleteNode(root, getArgsAsList(raw));
	}
}
