package me.lucyy.common.command;

import me.lucyy.common.CommonLibVersion;
import me.lucyy.common.format.TextFormatter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A builtin subcommand that shows the version of the plugin, alongside authors and
 * LucyCommonLib version.
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
        StringBuilder output = new StringBuilder();
        output.append(TextFormatter.formatTitle(plugin.getName(), provider)).append("\n");
        output.append(provider.formatMain(plugin.getName() + " version "));
        output.append(provider.formatAccent(plugin.getDescription().getVersion())).append("\n");
        output.append(provider.formatMain("Written by "));
        output.append(provider.formatAccent(plugin.getDescription().getAuthors().get(0))).append("\n");
        output.append(provider.formatMain("Built with LucyCommonLib version "));
        output.append(provider.formatAccent(CommonLibVersion.VERSION)).append("\n");
        output.append(TextFormatter.formatTitle("*", provider));
        sender.sendMessage(output.toString());
        return true;
    }
}
