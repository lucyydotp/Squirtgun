package me.lucyy.common.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexPattern implements FormatPattern {
	Pattern pattern = Pattern.compile("\\{(#[A-Fa-f0-9]{6})}");

	@Override
	public String process(String in) {
		Matcher matcher = pattern.matcher(in);
		while (matcher.find()) {
			String color = matcher.group(1);
			in = in.replace(matcher.group(), TextFormatter.colourFromText(color) + "");
		}
		return in;
	}
}
