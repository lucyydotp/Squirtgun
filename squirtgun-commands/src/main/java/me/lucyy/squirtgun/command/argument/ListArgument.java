package me.lucyy.squirtgun.command.argument;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * An argument that accepts a single string from a list.
 */
public class ListArgument extends AbstractArgument<String> {
	private final List<String> values;

	/**
	 * @param name this argument's name
	 * @param description this argument's description
	 * @param isOptional whether this argument will be displayed as optional
	 * @param values a set of acceptable values for this argument
	 */
	public ListArgument(String name, String description, boolean isOptional, List<String> values) {
		super(name, description, isOptional);
		this.values = values;
	}

	@Override
	public String getValue(Queue<String> args) {
		String val = args.poll();
		return val != null && values.contains(val) ? val : null;
	}

	@Override
	public @Nullable List<String> tabComplete(Queue<String> value) {
		String top = value.poll();
		if (top == null) return null;
		return values.stream()
				.filter(x -> x.startsWith(top))
				.collect(Collectors.toList());
	}
}
