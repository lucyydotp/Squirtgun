package me.lucyy.common.format;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RgbGradientPattern implements FormatPattern {
	Pattern pattern = Pattern.compile("\\{(#[A-Fa-f0-9]{6}):?([klmno]+)?>}(.*)\\{(#[A-Fa-f0-9]{6})<}");

	public static String fade(String col1, String col2, String formats, String text) {
		Color color1 = Color.decode(col1);
		Color color2 = Color.decode(col2);

		StringBuilder output = new StringBuilder();

		int[] reds = TextFormatter.fade(text.length(), color1.getRed(), color2.getRed());
		int[] greens = TextFormatter.fade(text.length(), color1.getGreen(), color2.getGreen());
		int[] blues = TextFormatter.fade(text.length(), color1.getBlue(), color2.getBlue());

		StringBuilder formatters = new StringBuilder();
		if (formats != null) {
			for (char character : formats.toCharArray()) formatters.append("&").append(character);
		}
		for (int x = 0; x < text.length(); x++) {
			output.append(ChatColor.of(new Color(reds[x], greens[x], blues[x])));
			output.append(formatters.toString());
			output.append(text.charAt(x));
		}

		return output.toString();
	}

	@Override
	public String process(String in) {
		Matcher matcher = pattern.matcher(in);
		while (matcher.find()) {
			String col1 = matcher.group(1);
			String formatters = matcher.group(2);
			String text = matcher.group(3);
			String col2 = matcher.group(4);
			in = in.replace(matcher.group(), fade(col1, col2, formatters, text));
		}
		return in;
	}
}
