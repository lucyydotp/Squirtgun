package me.lucyy.common.command;

/**
 * Provides formatting info to a command.
 */
public interface FormatProvider {
	/**
	 * Get a colour sequence for use in body text.
	 * @return a preformatted colour sequence
	 */
	String getMainColour();

	/**
	 * Get a colour sequence for use in emphasised text.
	 * @return a preformatted colour sequence
	 */
	String getAccentColour();

	/**
	 * Get a prefix, which is put before most command messages.
	 * @return a preformatted colour sequence
	 */
	String getPrefix();
}
