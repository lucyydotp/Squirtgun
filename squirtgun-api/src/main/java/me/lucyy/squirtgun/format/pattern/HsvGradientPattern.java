/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.lucyy.squirtgun.format.pattern;

import me.lucyy.squirtgun.format.TextFormatter;
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
