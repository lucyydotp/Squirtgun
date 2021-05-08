package me.lucyy.squirtgun.command.argument;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import me.lucyy.squirtgun.command.context.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;
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
		return args.poll();
	}

	@Override
	public @NotNull List<String> tabComplete(Queue<String> value) {
		value.poll();
		return ImmutableList.of("<" + getName() + ">");
	}

}
