package me.lucyy.squirtgun.command.context;

import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A command context based on a raw string.
 */
public class StringContext<T extends PermissionHolder> implements CommandContext<T> {

	private final FormatProvider provider;
	private final T target;
	private final CommandNode<T> node;
	private final String raw;
	private final LinkedHashMap<CommandArgument<?>, Object> argValues = new LinkedHashMap<>();
	private CommandNode<T> tail;

	private void populateArguments(CommandNode<T> node, Queue<String> raw, boolean findValues) {
		tail = node;
		for (CommandArgument<?> arg : node.getArguments()) {
			argValues.put(arg, findValues ? arg.getValue(raw) : null);
		}
		CommandNode<T> next = node.next(this);
		if (next == null) return;

		String perm = next.getPermission();
		if (perm != null && !getTarget().hasPermission(perm)) return;

		populateArguments(next, raw, findValues);
	}

	private Queue<String> getArgsAsList(String value) {
		return new LinkedList<>(Arrays.asList(value.split(" ", -1)));
	}

	public StringContext(FormatProvider provider, T target, CommandNode<T> node, String value) {
		this.node = node;
		this.raw = value;
		this.provider = provider;
		this.target = target;
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
	public @Nullable Object getArgumentValue(String name) {
		Optional<CommandArgument<?>> argument = argValues.keySet().stream()
				.filter(arg -> arg.getName().equals(name))
				.findFirst();
		if (!argument.isPresent()) {
			throw new IllegalArgumentException(String.format("Argument %s does not exist", name));
		}
		return getArgumentValue(argument.get());
	}

	@Override
	public String getRaw() {
		return raw;
	}

	@Override
	public @NotNull FormatProvider getFormat() {
		return provider;
	}

	@Override
	public List<String> tabComplete() {
		Queue<String> rawQueue = getArgsAsList(raw);

		// second call to getArgsAsList() to prevent consumption of the queue
		populateArguments(node, getArgsAsList(raw), true);
		List<List<String>> results = new ArrayList<>();
		List<CommandArgument<?>> argTree = new ArrayList<>(argValues.keySet());

		int argIdx = 0;
		while (!rawQueue.isEmpty()) {
			if (argIdx >= argTree.size()) return null;
			CommandArgument<?> arg = argTree.get(argIdx);
			results.add(arg.tabComplete(rawQueue));
			argIdx++;
		}
		return results.get(results.size() - 1);
	}

	@Override
	public Component execute() {
		populateArguments(node, getArgsAsList(raw), true);

		for (CommandArgument<?> argument : getTail().getArguments()) {
			if (argument.isOptional() || getArgumentValue(argument) != null) continue;
			return getFormat().getPrefix().append(
					getFormat().formatMain("Usage: " + getTail().getName() + " " +
							getTail().getArguments().stream()
									.map(Object::toString)
									.collect(Collectors.joining(" "))
					)
			);
		}
		return getTail().execute(this);
	}

	@Override
	public CommandNode<T> getTail() {
		return tail;
	}
}
