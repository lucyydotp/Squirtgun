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
		sender.sendMessage(provider.formatAccent(plugin.getName() + " v"
				+ plugin.getDescription().getVersion())
				+ provider.formatMain(" by ")
				+ provider.formatAccent(plugin.getDescription().getAuthors().get(0)));

		sender.sendMessage(provider.formatMain("Commands:"));
		cmd.getUserSubcommands(sender).forEach(cmd -> {
					if (cmd.getPermission() == null ||  sender.hasPermission(cmd.getPermission()))
						sender.sendMessage(provider.formatMain("/profile ")
								+ provider.formatAccent( cmd.getName())
								+ provider.formatMain(" - " + cmd.getDescription()));
				}
		);
		return true;
	}
}
