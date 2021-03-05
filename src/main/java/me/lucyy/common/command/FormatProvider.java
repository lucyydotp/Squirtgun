package me.lucyy.common.command;

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
	String formatMain(String input);

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
	String formatAccent(String input);

	/**
	 * Get a prefix, which is put before most command messages.
	 * @return a preformatted colour sequence
	 */
	String getPrefix();
}
