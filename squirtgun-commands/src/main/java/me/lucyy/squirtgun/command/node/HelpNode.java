package me.lucyy.squirtgun.command.node;

import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A command node that shows a help screen for another node
 * that shows its description, usage, and argument information.
 *
 * @since 2.0.0
 */
public class HelpNode<T extends PermissionHolder> implements CommandNode<T> {

	private final CommandNode<?> parentNode;

	public HelpNode(CommandNode<?> parentNode) {
		this.parentNode = parentNode;
	}

	@Override
	public @Nullable Component execute(CommandContext<T> context) {
		FormatProvider format = context.getFormat();
		Component out = Component.empty()
				.append(TextFormatter.formatTitle("Command Help", format))
				.append(Component.newline());

		String fullCommand = parentNode.getName() + " " +
				parentNode.getArguments().stream()
						.map(Object::toString)
						.collect(Collectors.joining());

		out = out.append(TextFormatter.centreText(parentNode.getDescription(), format, " "))
				.append(Component.newline())
				.append(format.formatAccent("Usage"))
				.append(format.formatMain(": " + fullCommand))
				.append(Component.newline());

		if (parentNode.getArguments().size() != 0) {
			out = out.append(Component.newline())
					.append(TextFormatter.centreText("Arguments", format, " "))
					.append(Component.newline());
			for (CommandArgument<?> argument : parentNode.getArguments()) {
				out = out.append(format.formatAccent(argument.getName()))
						.append(format.formatMain( " - " + argument.getDescription()))
						.append(Component.newline());
			}
		}

		out = out.append(Component.newline())
				.append(TextFormatter.formatTitle("*", context.getFormat()));

		return out;
	}

	@Override
	public @NotNull String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Shows help on using this command.";
	}

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return Collections.emptyList();
	}
}
