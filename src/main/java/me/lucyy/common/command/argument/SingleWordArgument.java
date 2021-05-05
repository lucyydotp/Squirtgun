package me.lucyy.common.command.argument;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import me.lucyy.common.command.CommandContext;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Queue;

/**
 * An argument that accepts a single word. If the argument is missing, throws a NullPointerException.
 */
public final class SingleWordArgument extends AbstractArgument<String> {
	public SingleWordArgument(String name, String description) {
		super(name, description);
	}

	@Override
	public String getValue(Queue<String> args) {
		String value = args.peek();
		Preconditions.checkNotNull(value);
		return value;
	}

	@Override
	public @NotNull List<String> tabComplete(CommandContext<String> context) {
		return ImmutableList.of("<" + getName() + ">");
	}
}
