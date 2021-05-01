package me.lucyy.common.format.pattern;

import me.lucyy.common.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * The hex pattern, in the format {@literal {#rrggbb}text}.
 * This is an internal class, you shouldn't need to use it.
 */
public final class HexPattern implements FormatPattern {

	@Override
	public Component process(final @NotNull String in, final String formats) {
		if (in.length() < 8) return null;
		if (!in.startsWith("{#") || in.charAt(8) != '}') return null;
		for (int i = 2; i < 8; i++) {
			char c = in.charAt(i);
			if ((c < '0' || c > '9') && (c < 'A' || c > 'F') && (c < 'a' || c > 'f')) return null;
		}
		final TextColor colour = TextFormatter.colourFromText(in.substring(1, 8));
		assert colour != null; // this is just to shut intellij up

		Component component = Component.text(in.substring(9), TextColor.color(colour));
		if (formats != null) {
			for (char c : formats.toCharArray()) {
				//noinspection ConstantConditions - the char is checked in regex
				component = component.decorate(LegacyComponentSerializer.parseChar(c).decoration());
			}
		}
		return component;
	}
}
