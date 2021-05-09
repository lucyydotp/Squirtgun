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

	private List<CommandArgument<?>> buildArgTree(CommandNode<T> node, List<CommandArgument<?>> args) {
		args.addAll(node.getArguments());
		CommandNode<T> next = node.next(this);
		if (next != null) buildArgTree(next, args);
		return args;
	}

	private Queue<String> getArgsAsList(String value) {
		return new LinkedList<>(Arrays.asList(value.split(" ", -1)));
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
		Queue<String> rawQueue = getArgsAsList(raw);
		List<List<String>> results = new ArrayList<>();
		List<CommandArgument<?>> argTree = buildArgTree(root, new ArrayList<>());

		int argIdx = 0;
		while (!rawQueue.isEmpty()) {
			if (argIdx >= argTree.size()) return null;
			CommandArgument<?> arg = argTree.get(argIdx);
			results.add(arg.tabComplete(rawQueue));
			argIdx++;
		}
		return results.get(results.size() - 1);
	}
}
