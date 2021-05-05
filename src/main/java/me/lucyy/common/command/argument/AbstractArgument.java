package me.lucyy.common.command.argument;

public abstract class AbstractArgument<T> implements CommandArgument<T> {
	private final String name;
	private final String description;

	protected AbstractArgument(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final String getDescription() {
		return description;
	}
}
