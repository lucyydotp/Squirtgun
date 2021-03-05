package me.lucyy.common.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A builtin subcommand that will print the plugin name, version, author, and a
 * list of subcommands with their descriptions.
 */
public class HelpSubcommand implements Subcommand {

	private final Command cmd;
	private final FormatProvider provider;
	private final JavaPlugin plugin;

	public HelpSubcommand(Command cmd, FormatProvider provider, JavaPlugin plugin) {
		this.cmd = cmd;
		this.provider = provider;
		this.plugin = plugin;
	}


	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "List all subcommands for the plugin.";
	}

	@Override
	public String getUsage() {
		return "help";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public boolean execute(CommandSender sender, CommandSender target, String[] args) {
		sender.sendMessage(provider.getAccentColour()
				+ plugin.getName() + " v"
				+ plugin.getDescription().getVersion()
				+ provider.getMainColour() + " by "
				+ provider.getAccentColour() + plugin.getDescription().getAuthors().get(0));

		sender.sendMessage(provider.getMainColour() + "Commands:");
		cmd.getUserSubcommands(sender).forEach(cmd -> {
					if (cmd.getPermission() == null ||  sender.hasPermission(cmd.getPermission()))
						sender.sendMessage(provider.getMainColour() + "/profile "
								+ provider.getAccentColour() + cmd.getName()
								+ provider.getMainColour() + " - " + cmd.getDescription());
				}
		);
		return true;
	}
}
