package me.lucyy.squirtgun.command.argument;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Queue;

/**
 * An argument that accepts a single word. If missing, returns null.
 */
public final class SingleWordArgument extends AbstractArgument<String> {

	/**
	 * @param name        the argument's name
	 * @param description the argument's description
	 * @param isOptional  whether the argument is optional
	 */
	public SingleWordArgument(String name, String description, boolean isOptional) {
		super(name, description, isOptional);
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
