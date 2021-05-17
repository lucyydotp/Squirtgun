package me.lucyy.squirtgun.bukkit;

import com.google.common.collect.ImmutableList;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.context.StringContext;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.PermissionHolder;
import me.lucyy.squirtgun.platform.SquirtgunPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * Wraps a {@link CommandNode} so it can be executed by a Bukkit server.
 */
public class BukkitNodeExecutor implements TabExecutor {

	private final CommandNode<PermissionHolder> node;
	private final FormatProvider formatter;

	/**
	 * @param node      the root command node to execute
	 * @param formatter the command sender to pass to the context
	 */
	public BukkitNodeExecutor(CommandNode<PermissionHolder> node, FormatProvider formatter) {
		this.node = node;
		this.formatter = formatter;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender bukkitSender,
	                         @NotNull Command command,
	                         @NotNull String label,
	                         @NotNull String[] args) {
		PermissionHolder sender;
		if (bukkitSender instanceof OfflinePlayer) {
			sender = new BukkitPlayer((OfflinePlayer) bukkitSender);
		} else {
			sender = new BukkitSenderWrapper(bukkitSender);
		}

		Component ret = new StringContext<>(formatter,
				sender, node, String.join(" ", args)).execute();
		if (ret != null) {
			bukkitSender.sendMessage(ret);
		}
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
	                                            @NotNull Command command,
	                                            @NotNull String alias,
	                                            @NotNull String[] stringArgs) {
		CommandContext<PermissionHolder> context = new StringContext<>(
				formatter, new BukkitSenderWrapper(sender), node, String.join(" ", stringArgs));
		List<String> ret = context.tabComplete();
		return ret == null ? ImmutableList.of() : ret;
	}
}
