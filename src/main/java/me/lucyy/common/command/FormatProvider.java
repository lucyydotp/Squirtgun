package me.lucyy.common.command;

/**
 * Provides formatting info to a command.
 */
public interface FormatProvider {

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
