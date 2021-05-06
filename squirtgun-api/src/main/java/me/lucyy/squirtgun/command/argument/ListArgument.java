package me.lucyy.squirtgun.command.argument;

import me.lucyy.squirtgun.command.CommandContext;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class ListArgument extends AbstractArgument<String> {
	private final List<String> values;

	public ListArgument(String name, String description, List<String> values) {
		super(name, description);
		this.values = values;
	}

	@Override
	public String getValue(Queue<String> args) {
		String val = args.poll();
		return values.contains(val) ? val : null;
	}

	@Override
	@SuppressWarnings("ConstantConditions") // value of self should never be null when tabcompleting
	public @Nullable List<String> tabComplete(CommandContext<String> context) {
		String value = context.getArgumentValue(this);
		return values.stream()
				.filter(x -> x.startsWith(value))
				.collect(Collectors.toList());
	}
}
