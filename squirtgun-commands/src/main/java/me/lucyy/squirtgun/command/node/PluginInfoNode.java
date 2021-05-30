package me.lucyy.squirtgun.command.node;

import me.lucyy.squirtgun.Squirtgun;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import me.lucyy.squirtgun.platform.PermissionHolder;
import me.lucyy.squirtgun.platform.Platform;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;

public class PluginInfoNode<T extends PermissionHolder> implements CommandNode<T> {

	private final String name;
	private final Platform plugin;

	public PluginInfoNode(String name, Platform plugin) {
		this.name = name;
		this.plugin = plugin;
	}

	@Override
	public @Nullable Component execute(CommandContext<T> context) {
		final FormatProvider fmt = context.getFormat();
		final Component nl = Component.newline();
		return Component.empty()
				.append(TextFormatter.formatTitle(plugin.getPluginName(), fmt))
				.append(nl).append(nl)
				.append(fmt.formatMain(plugin.getPluginName() + " version "))
				.append(fmt.formatAccent(plugin.getPluginVersion())).append(nl)
				.append(fmt.formatMain("Written by "))
				.append(fmt.formatAccent(String.join(", ", plugin.getAuthors())))
				.append(nl)
				.append(fmt.formatMain("Built with Squirtgun version "))
				.append(fmt.formatAccent(Squirtgun.VERSION)).append(nl).append(nl)
				.append(TextFormatter.formatTitle("*", fmt));
	}

	@Override
	public @NotNull String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return "Shows information about this plugin.";
	}
}
