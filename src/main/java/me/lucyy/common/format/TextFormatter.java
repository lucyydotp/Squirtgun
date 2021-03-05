package me.lucyy.common.format;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import javax.xml.soap.Text;
import java.text.Format;
import java.util.Arrays;
import java.util.List;

/**
 * Formats text, supporting gradients and hex codes.
 * This was very heavily inspired by IridiumColorAPI
 * https://github.com/Iridium-Development/IridiumColorAPI
 */
public class TextFormatter {

	private static final List<FormatPattern> patterns = Arrays.asList(new RgbGradientPattern(),
			new HsvGradientPattern(), new HexPattern());

	/**
	 * Parse a string colour representation to a {@link ChatColor}.
	 *
	 * @param in a string representation, either as:
	 *           <ul>
	 *           <li>a single character as in a standard formatter code</li>
	 *           <li>a 6-digit HTML hex code, prepended with # ie #ff00ff</li>
	 *           </ul>
	 * @return the ChatColor representation, or null if it could not be parsed
	 */
	public static ChatColor colourFromText(String in) {
		if (in.length() == 1) return ChatColor.getByChar(in.charAt(0));
		else if (in.length() == 7 && in.startsWith("#")) {
			try {
				return ChatColor.of(in);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
		return null;
	}

	/*
	public static boolean isFormatter(ChatColor col) {
		return col == ChatColor.BOLD || col == ChatColor.ITALIC || col == ChatColor.UNDERLINE
				|| col == ChatColor.STRIKETHROUGH || col == ChatColor.MAGIC;
	}

	public static void setFormat(BaseComponent in, ChatColor col) {
		if (col == ChatColor.BOLD) in.setBold(true);
		else if (col == ChatColor.ITALIC) in.setItalic(true);
		else if (col == ChatColor.UNDERLINE) in.setUnderlined(true);
		else if (col == ChatColor.STRIKETHROUGH) in.setStrikethrough(true);
		else if (col == ChatColor.MAGIC) in.setObfuscated(true);
		else if (col != ChatColor.RESET) in.setColor(col);
	}*/

	public static BaseComponent[] format(String input) {
		String output = input;
		for (FormatPattern pattern : patterns) {
			output = pattern.process(output);
		}
		return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', output));
	}

	public static int[] fade(int steps, int val1, int val2) {
		float step = (val2 - val1) / (float) (steps - 1);

		int[] output = new int[steps];

		for (int x = 0; x < steps; x++) {
			float result = (val1) + step * x;
			while (result < 0) result += 360;
			while (result > 360) result -= 360;
			output[x] = Math.round(result);
		}
		return output;
	}
}