package me.lucyy.squirtgun.command.node;

import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SubcommandHelpNode<T extends PermissionHolder> implements CommandNode<T> {

	private final SubcommandNode<?> parentNode;

	public SubcommandHelpNode(SubcommandNode<?> parentNode) {
		this.parentNode = parentNode;
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
	public @Nullable Component execute(CommandContext<T> context) {
		final FormatProvider fmt = context.getFormat();
		Component out = Component.empty()
				.append(TextFormatter.formatTitle("Commands:", fmt))
				.append(Component.text("\n"));

		for (CommandNode<?> node : parentNode.getNodes()) {
			String perm = node.getPermission();
			if (perm == null || context.getTarget().hasPermission(perm)) {
				Component innerComp = fmt.formatAccent(node.getName())
						.append(fmt.formatMain(" - " + node.getDescription()))
						.append(Component.text("\n"));
				out = out.append(innerComp);
			}
		}

		return out;
	}
}
