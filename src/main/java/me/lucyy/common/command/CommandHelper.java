package me.lucyy.common.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandHelper {

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

	/**
	 * Concatenates an array of strings to a single space-delimited string.
	 *
	 * @param args  the strings to concatenate
	 * @param start the index to start from
	 * @return a concatenated string
	 */
	public static String concatArgs(String[] args, int start) {
		StringBuilder value = new StringBuilder();
		for (int x = start; x < args.length; x++) {
			value.append(args[x]).append(" ");
		}
		value.setLength(value.length() - 1);
		return value.toString();
	}
}
