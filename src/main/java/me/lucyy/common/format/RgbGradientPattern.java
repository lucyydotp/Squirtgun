package me.lucyy.common.format;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RgbGradientPattern implements FormatPattern {
	Pattern pattern = Pattern.compile("\\{(#[A-Fa-f0-9]{6})>}(.*)\\{(#[A-Fa-f0-9]{6})<}");

	private String fadeRgb(String col1, String col2, String text) {
		Color color1 = Color.decode(col1);
		Color color2 = Color.decode(col2);

		StringBuilder output = new StringBuilder();

		int[] reds = TextFormatter.fade(text.length(), color1.getRed(), color2.getRed());
		int[] greens = TextFormatter.fade(text.length(), color1.getGreen(), color2.getGreen());
		int[] blues = TextFormatter.fade(text.length(), color1.getBlue(), color2.getBlue());

		for (int x = 0; x < text.length(); x++) {
			output.append(ChatColor.of(new Color(reds[x], greens[x], blues[x])));
			output.append(text.charAt(x));
		}

		return output.toString();
	}

	@Override
	public String process(String in) {
		Matcher matcher = pattern.matcher(in);
		while (matcher.find()) {
			String col1 = matcher.group(1);
			String text = matcher.group(2);
			String col2 = matcher.group(3);
			in = in.replace(matcher.group(), fadeRgb(col1, col2, text));
		}
		return in;
	}
}
