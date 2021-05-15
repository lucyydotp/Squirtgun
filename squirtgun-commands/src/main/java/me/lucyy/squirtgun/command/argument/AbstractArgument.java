package me.lucyy.squirtgun.command.argument;

/**
 * An abstract argument, encapsulating the name and description fields.
 */
public abstract class AbstractArgument<T> implements CommandArgument<T> {
	private final String name;
	private final String description;

	/**
	 * @param name the argument's name
	 * @param description the argument's description
	 */
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
