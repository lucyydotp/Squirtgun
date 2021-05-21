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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The RGB gradient pattern, fading between two RGB codes in the format {@literal {#rrggbb>}text{#rrggbb<}}.
 * This is an internal class, you shouldn't need to use it.
 */
public final class RgbGradientPattern implements FormatPattern {
	final Pattern pattern = Pattern.compile("\\{(#[A-Fa-f0-9]{6}):?([klmno]+)?>}(.*)\\{(#[A-Fa-f0-9]{6})<}");

	private static int clamp(final int in) {
		if (in > 255) return 255;
		return Math.max(in, 0);
	}

	private static Component fade(final String col1, final String col2, final String formats, final String text) {
		Component component = Component.empty();

		final TextColor color1 = TextColor.fromCSSHexString(col1);
		assert color1 != null;

		// special case for single-length characters
		if (text.length() == 1) return component.append(Component.text(text).color(color1));

		final TextColor color2 = TextColor.fromCSSHexString(col2);
		assert color2 != null;

		final int[] reds = TextFormatter.fade(text.length(), color1.red(), color2.red());
		final int[] greens = TextFormatter.fade(text.length(), color1.green(), color2.green());
		final int[] blues = TextFormatter.fade(text.length(), color1.blue(), color2.blue());


		for (int x = 0; x < text.length(); x++) {
			final TextColor color = TextColor.color(clamp(reds[x]), clamp(greens[x]), clamp(blues[x]));
			component = component.append(Component.text(text.charAt(x), color));
		}

		return TextFormatter.applyLegacyDecorations(component, formats); // TODO test this
	}

	@Override
	public @Nullable Component process(@NotNull String in, String formatter) {
		final Matcher matcher = pattern.matcher(in);
		if (!matcher.find()) return null;

		final String col1 = matcher.group(1);
		String formatters = formatter;
		if (formatters == null) formatters = matcher.group(2);
		final String text = matcher.group(3);
		final String col2 = matcher.group(4);
		return fade(col1, col2, formatters, text);
	}
}
