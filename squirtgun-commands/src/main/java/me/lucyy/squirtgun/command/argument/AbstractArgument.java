package me.lucyy.squirtgun.command.argument;

/**
 * An abstract argument, encapsulating the name and description fields.
 */
public abstract class AbstractArgument<T> implements CommandArgument<T> {
	private final String name;
	private final String description;
	private final boolean optional;

	/**
	 * @param name the argument's name
	 * @param description the argument's description
	 */
	protected AbstractArgument(String name, String description, boolean isOptional) {
		this.name = name;
		this.description = description;
		this.optional = isOptional;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final String getDescription() {
		return description;
	}

	@Override
	public boolean isOptional() {
		return optional;
	}

	@Override
	public String toString() {
		return isOptional() ? "[" + getName() + "]" :  "<" + getName() + ">";
	}
}
