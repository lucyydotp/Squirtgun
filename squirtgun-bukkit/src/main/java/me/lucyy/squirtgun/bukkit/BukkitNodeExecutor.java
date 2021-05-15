package me.lucyy.squirtgun.bukkit;

import com.google.common.collect.ImmutableList;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.context.StringContext;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class BukkitNodeExecutor implements TabExecutor {

	private final CommandNode<PermissionHolder> node;
	private final FormatProvider formatter;

	public BukkitNodeExecutor(CommandNode<PermissionHolder> node, FormatProvider formatter) {
		this.node = node;
		this.formatter = formatter;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender,
	                         @NotNull Command command,
	                         @NotNull String label,
	                         @NotNull String[] args) {
		Component ret = node.execute(new StringContext<>(formatter,
				new BukkitSenderWrapper(sender), node, String.join(" ", args)));
		if (ret != null) {
			sender.sendMessage(ret);
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
