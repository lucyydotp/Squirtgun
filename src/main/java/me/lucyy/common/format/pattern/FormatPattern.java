package me.lucyy.common.format.pattern;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A pattern that replaces string text into a component.
 */
public interface FormatPattern {
	/**
	 * Parses the given string to a component.
	 *
	 * @param in                 the string to parse
	 * @param formattersOverride a string containing vanilla formatters, ie 'lo', to apply to the text
	 * @return the component, or null if the string doesnt match the format
	 */
	@Nullable
	Component process(@NotNull final String in, @Nullable final String formattersOverride);
}
