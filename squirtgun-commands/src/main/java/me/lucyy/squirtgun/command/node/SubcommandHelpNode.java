package me.lucyy.squirtgun.command.node;

import com.google.common.collect.ImmutableList;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.argument.ListArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubcommandHelpNode<T extends PermissionHolder> implements CommandNode<T> {

	private final SubcommandNode<?> parentNode;
	private final CommandArgument<String> childArgument;

	public SubcommandHelpNode(SubcommandNode<?> parentNode) {
		this.parentNode = parentNode;
		childArgument = new ListArgument("command",
				"The command to get help for.",
				true,
				parentNode.getNodes().stream()
						.map(CommandNode::getName)
						.collect(Collectors.toList())
				);
	}

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return ImmutableList.of(childArgument);
	}

	@Override
	public @NotNull String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Shows this screen.";
	}

	@Override
	public @Nullable CommandNode<T> next(CommandContext<T> context) {
		String child = context.getArgumentValue(childArgument);
		if (child == null) return null;

		Optional<? extends CommandNode<?>> nodeWithGivenName = parentNode.getNodes().stream()
				.filter(node -> node.getName().equals(child))
				.findFirst();

		return nodeWithGivenName.<CommandNode<T>>map(HelpNode::new).orElse(null);

	}

	@Override
	public @Nullable Component execute(CommandContext<T> context) {
		final FormatProvider fmt = context.getFormat();
		Component out = Component.empty()
				.append(TextFormatter.formatTitle("Commands:", fmt))
				.append(Component.newline());

		for (CommandNode<?> node : parentNode.getNodes()) {
			String perm = node.getPermission();
			if (perm == null || context.getTarget().hasPermission(perm)) {
				Component innerComp = fmt.formatAccent(node.getName())
						.append(fmt.formatMain(" - " + node.getDescription()))
						.append(Component.text("\n"));
				out = out.append(innerComp);
			}
		}

		out = out.append(Component.newline())
				.append(TextFormatter.formatTitle("*", fmt));

		return out;
	}
}
