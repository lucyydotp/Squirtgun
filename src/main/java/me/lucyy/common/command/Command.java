package me.lucyy.common.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * A root command that other subcommands stem from.
 */

public class Command implements CommandExecutor, TabCompleter {
    private final FormatProvider format;
    private final HashMap<String, Subcommand> subcommands = new HashMap<>();

    private Subcommand defaultSubcommand;

    public Command(FormatProvider format) {
        this.format = format;
    }

    /**
     * Registers a subcommand.
     *
     * @param cmd the subcommand to register
     */
    public void register(Subcommand cmd) {
        subcommands.put(cmd.getName(), cmd);
    }

    /**
     * Sets the subcommand to run if no subcommand is specified or is invalid.
     */
    public void setDefaultSubcommand(Subcommand defaultSubcommand) {
        this.defaultSubcommand = defaultSubcommand;
    }

    /**
     * Gets the default subcommand, or null if it's not set.
     */
    public Subcommand getDefaultSubcommand() {
        return defaultSubcommand;
    }

    /**
     * Gets the subcommands a sender has permission to execute.
     *
     * @param sender the sender to check permissions against
     */
    public List<Subcommand> getUserSubcommands(CommandSender sender) {
        List<Subcommand> cmds = new ArrayList<>();
        subcommands.forEach((String label, Subcommand cmd) -> {
                    if (cmd.getPermission() == null || sender.hasPermission(cmd.getPermission()))
                        cmds.add(cmd);
                }
        );
        return cmds;
    }

    /**
     * Gets the subcommands a sender has permission to execute, as in {@link #getUserSubcommands(CommandSender)},
     * as a map where the key is the command name.
     *
     * @param sender the sender to check permissions against
     */
    public Map<String, Subcommand> getUserSubcommandsMap(CommandSender sender) {
        Map<String, Subcommand> cmds = new HashMap<>();
        subcommands.forEach((String label, Subcommand cmd) -> {
                    if (cmd.getPermission() == null || sender.hasPermission(cmd.getPermission()))
                        cmds.put(label, cmd);
                }
        );
        return cmds;
    }

    /**
     * Get all registered subcommand names.
     *
     * @return a list of the names of all registered subcommands
     */
    public List<String> getSubcommands() {
        return new ArrayList<>(subcommands.keySet());
    }

    private void showDefault(CommandSender sender) {
        getDefaultSubcommand().execute(sender, sender, new String[]{});
    }

    /**
     * Inherited from {@link CommandExecutor}. This method should probably not be called.
     */
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return onCommand(sender, sender, label, args);
    }

    /**
     * Used as part of command handling. This method should probably not be called.
     */
    @SuppressWarnings("SameReturnValue")
	public boolean onCommand(CommandSender sender, CommandSender target, String cmdName, String[] args) {
        if (args.length < 1) {
            showDefault(sender);
            return true;
        }

        Subcommand subcommand = subcommands.get(args[0]);

        if (subcommand == null) {
            showDefault(sender);
            return true;
        }

        if (subcommand.getPermission() == null || sender.hasPermission(subcommand.getPermission())) {
            if (!subcommand.execute(sender, target, Arrays.copyOfRange(args, 1, args.length))) {
                sender.sendMessage(format.getPrefix() + format.formatMain("Usage: /" + cmdName + " " + subcommand.getUsage()));
            }
        } else {
            sender.sendMessage(format.getPrefix() + format.formatMain("No permission!"));
        }

        return true;
    }

    /**
     * Inherited from {@link CommandExecutor}. This method should probably not be called.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> results = new ArrayList<>();
            getUserSubcommands(sender).forEach(x -> results.add(x.getName()));
            return results;
        }

        Subcommand subcmd = getUserSubcommandsMap(sender).get(args[0]);
        if (subcmd == null) return null;
        return subcmd.tabComplete(args);
    }

    /**
     * Returns a list of player names starting with the input, case insensitive.
     *
     * @param name the partial name to check for
     * @return a list of names that match. An empty list if none were found.
     */
    public static List<String> tabCompleteNames(String name) {
        List<String> out = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toUpperCase().startsWith(name.toUpperCase(Locale.ROOT))) out.add(player.getName());
        }
        return out;
    }
}
