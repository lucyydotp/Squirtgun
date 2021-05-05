package me.lucyy.common.command.old;

import me.lucyy.common.CommonLibVersion;
import me.lucyy.common.command.FormatProvider;
import me.lucyy.common.format.Platform;
import me.lucyy.common.format.TextFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A builtin subcommand that shows the version of the plugin, alongside authors and
 * LucyCommonLib version.
 * @deprecated
 */
public class VersionSubcommand implements Subcommand {

	private final FormatProvider provider;
	private final JavaPlugin plugin;

	public VersionSubcommand(FormatProvider provider, JavaPlugin plugin) {
		this.provider = provider;
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public String getDescription() {
		return "Shows version information.";
	}

	@Override
	public String getUsage() {
		return "version";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public boolean execute(CommandSender sender, CommandSender target, String[] args) {
		Component comp = Component.empty()
			.append(TextFormatter.formatTitle(plugin.getName(), provider))
			.append(Component.text("\n"));


		Component nl = Component.text("\n");

		comp = comp.append(provider.formatMain(plugin.getName() + " version "))
			.append(provider.formatAccent(plugin.getDescription().getVersion())).append(nl)
			.append(provider.formatMain("Written by "))
			.append(provider.formatAccent(plugin.getDescription().getAuthors().get(0))).append(nl)
			.append(provider.formatMain("Built with LucyCommonLib version "))
			.append(provider.formatAccent(CommonLibVersion.VERSION)).append(nl)
			.append(TextFormatter.formatTitle("*", provider));

		Platform.send(sender, comp);
		return true;
	}
}
