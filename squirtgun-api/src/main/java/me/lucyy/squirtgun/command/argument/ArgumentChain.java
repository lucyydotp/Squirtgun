package me.lucyy.squirtgun.command.argument;

import me.lucyy.squirtgun.command.context.CommandContext;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * I havent been bothered to do javadocs yet, just know this is a complete and utter mess
 * @param <T>
 */
public class ArgumentChain<T> implements Iterable<CommandArgument<?>> {

	/**
	 * This exists for the sake of brevity and is equivalent to a
	 * {@literal Function<CommandContext<T>, CommandArgument<?>>}
	 *
	 * @param <T> the type of the context
	 */
	public interface ArgumentFunction<T> extends Function<
				CommandContext<T>,
				CommandArgument<?>> {}

	public static class Builder<T> {
		private final List<ArgumentFunction<T>> functions = new ArrayList<>();
		private CommandContext<T> context;

		public Builder<T> context(CommandContext<T> context) {
			this.context = context;
			return this;
		}

		public Builder<T> then(ArgumentFunction<T> then) {
			functions.add(then);
			return this;
		}

		public Builder<T> then(CommandArgument<?> then) {
			functions.add(a -> then);
			return this;
		}

		public ArgumentChain<T> build() {
			return new ArgumentChain<T>(functions, context);
		}
	}

	public static <T> ArgumentChain<T> of(CommandContext<T> context, CommandArgument<?>... args) {
		return new ArgumentChain<T>(
				Arrays.stream(args)
				.map(x -> (ArgumentFunction<T>) (a -> x))
				.collect(Collectors.toList()),
				context
		);
	}

	private final List<ArgumentFunction<T>> argFunctions;
	private final CommandContext<T> context;

	public ArgumentChain(List<ArgumentFunction<T>> args, CommandContext<T> context) {
		argFunctions = args;
		this.context = context;
	}

	public <U> U getValue(CommandArgument<U> value) {
		return null;
	}

	private class ChainIterator implements Iterator<CommandArgument<?>> {
		private int idx;
		private CommandArgument<?> last;

		@Override
		public boolean hasNext() {
			return idx < argFunctions.size();
		}

		@Override
		public CommandArgument<?> next() {
			last = argFunctions.get(idx++).apply(context);
			return last;
		}

		public CommandArgument<?> last() {
			return last;
		}
	}

	@NotNull
	@Override
	public Iterator<CommandArgument<?>> iterator() {
		return new ChainIterator();
	}
}
