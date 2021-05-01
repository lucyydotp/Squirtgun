package me.lucyy.common.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

/**
 * Provides formatting info to a command.
 */
public interface FormatProvider {

	/**
	 * Formats text using the main colour.
	 *
	 * @param input the text to format
	 * @return the formatted text
	 */
	default Component formatMain(@NotNull String input) {
		return formatMain(input, new TextDecoration[0]);
	}

	/**
	 * Formats text using the main colour.
	 *
	 * @param input      the text to format
	 * @param formatters a string of vanilla formatter codes to apply, for example "lo" for bold and italic
	 * @return the formatted text
	 */
	Component formatMain(@NotNull String input, @NotNull TextDecoration[] formatters);

	/**
	 * Formats text using the accent colour.
	 *
	 * @param input the text to format
	 * @return the formatted text
	 */
	default Component formatAccent(@NotNull String input) {
		return formatAccent(input, new TextDecoration[0]);
	}

	/**
	 * Formats text using the accent colour.
	 *
	 * @param input      the text to format
	 * @param formatters a string of vanilla formatter codes to apply, for example "lo" for bold and italic
	 * @return the formatted text
	 */
	Component formatAccent(@NotNull String input, @NotNull TextDecoration[] formatters);

	/**
	 * Get a prefix, which is put before most command messages.
	 *
	 * @return a preformatted colour sequence
	 */
	Component getPrefix();
}
