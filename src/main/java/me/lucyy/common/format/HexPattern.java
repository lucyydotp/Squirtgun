package me.lucyy.common.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexPattern implements FormatPattern {
	final Pattern pattern = Pattern.compile("\\{(#[A-Fa-f0-9]{6})}");

	@Override
	public String process(String in, String format) {
		Matcher matcher = pattern.matcher(in);
		while (matcher.find()) {
			String color = matcher.group(1);

			StringBuilder formatters = new StringBuilder();
			if (format != null) {
				for (char character : format.toLowerCase().toCharArray()) formatters.append("&").append(character);
			}

			in = in.replace(matcher.group(), TextFormatter.colourFromText(color) + formatters.toString());
		}
		return in;
	}
}
