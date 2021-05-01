package me.lucyy.common.format.pattern;

import me.lucyy.common.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The HSV gradient pattern, fading between two HSV hues with a consistent saturation and value
 * in the format {@literal {hsv:hhssvv>}text{hh<}}.
 * This is an internal class, you shouldn't need to use it.
 */
public final class HsvGradientPattern implements FormatPattern {
	final Pattern pattern = Pattern.compile("\\{hsv:([A-Fa-f0-9]{6}):?([klmno]+)?>}(.*)\\{([A-Fa-f0-9]{2})<}");

	private static float clamp(final float in) {
		if (in > 1f) return 1f;
		if (in < 0) return 0;
		return in;
	}

	private static Component fade(final int hue1, final int hue2, final int sat, final int val,
								  final String text, final String formats) {
		Component component = Component.empty();

		final int[] hues = TextFormatter.fade(text.length(), hue1, hue2);

		for (int x = 0; x < text.length(); x++) {
			TextColor color = TextColor.color(HSVLike.of(
				clamp(hues[x] / 255f) % 1, clamp(sat / 255f), clamp(val / 255f)));
			component = component.append(Component.text(text.charAt(x), color));
		}

		return TextFormatter.applyLegacyDecorations(component, formats);
	}

	@Override
	public Component process(final @NotNull String in, final String format) {
		final Matcher matcher = pattern.matcher(in);
		if (!matcher.find()) return null;
		final int hue1 = Integer.parseInt(matcher.group(1).substring(0, 2), 16);
		final int sat = Integer.parseInt(matcher.group(1).substring(2, 4), 16);
		final int val = Integer.parseInt(matcher.group(1).substring(4, 6), 16);

		final int hue2 = Integer.parseInt(matcher.group(4), 16);
		final String text = matcher.group(3);
		String formats = format;
		if (formats == null) formats = matcher.group(2);

		return fade(hue1, hue2, sat, val, text, formats);
	}
}
