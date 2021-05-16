package me.lucyy.squirtgun.command.context;

import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

/**
 * A command context based on a raw string.
 */
public class StringContext<T extends PermissionHolder> implements CommandContext<T> {

	private final FormatProvider provider;
	private final T target;
	private final String raw;
	private final LinkedHashMap<CommandArgument<?>, Object> argValues = new LinkedHashMap<>();
	private final CommandNode<T> root;
	private CommandNode<T> tail;

	private void populateArguments(CommandNode<T> node, Queue<String> raw) {
		tail = node;
		for (CommandArgument<?> arg : node.getArguments()) {
			argValues.put(arg, arg.getValue(raw));
		}
		CommandNode<T> next = node.next(this);
		if (next == null) return;

		String perm = next.getPermission();
		if (perm != null && !getTarget().hasPermission(perm)) return;

		populateArguments(next, raw);
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
		return getTail().execute(this);
	}

	@Override
	public CommandNode<T> getTail() {
		return tail;
	}
}
