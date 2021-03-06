package me.lucyy.common.command;

import net.md_5.bungee.api.chat.BaseComponent;

/**
 * Provides formatting info to a command.
 */
public interface FormatProvider {
	/**
	 * Get a colour sequence for use in body text.
	 * @deprecated Use {@link #formatMain(String)}
	 * @return a preformatted colour sequence
	 */
	String getMainColour();

	/**
	 * Formats text using the main colour.
	 * @param input the text to format
	 * @return the formatted text
	 */
	default String formatMain(String input) {
		return formatMain(input, "");
	}

	/**
	 * Formats text using the main colour.
	 * @param input the text to format
	 * @param formatters a string of vanilla formatter codes to apply, for example "lo" for bold and italic
	 * @return the formatted text
	 */
	String formatMain(String input, String formatters);

	/**
	 * Get a colour sequence for use in emphasised text.
	 * @deprecated Use {@link #formatAccent(String)}
	 * @return a preformatted colour sequence
	 */
	String getAccentColour();

	/**
	 * Formats text using the accent colour.
	 * @param input the text to format
	 * @return the formatted text
	 */
	default String formatAccent(String input) {
		return formatAccent(input, "");
	}

	/**
	 * Formats text using the accent colour.
	 * @param input the text to format
	 * @param formatters a string of vanilla formatter codes to apply, for example "lo" for bold and italic
	 * @return the formatted text
	 */
	String formatAccent(String input, String formatters);

	/**
	 * Get a prefix, which is put before most command messages.
	 * @return a preformatted colour sequence
	 */
	String getPrefix();
}
