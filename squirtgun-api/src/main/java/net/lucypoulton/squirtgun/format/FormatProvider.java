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

package net.lucypoulton.squirtgun.format;

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
     * Formats title text. This defaults to the old TextFormatter#formatTitle behaviour (centre-aligned title text with
     * solid accent-coloured bars either side). This should be overridden for the Discord platform as centre-alignment
     * does not work there due to a different font.
     *
     * @param input the text to act as the title
     * @return the formatted text
     * @since 2.0.0
     */
    default Component formatTitle(String input) {
        return TextFormatter.centreText(input, this, " ", new TextDecoration[]{TextDecoration.STRIKETHROUGH});
    }

    /**
     * Formats text into a footer, intended to be shown as the last line of a message. By default, this behaves the same
     * as {@link #formatTitle(String)}.
     *
     * @param input the text to act as the footer
     * @return the formatted text
     * @since 2.0.0
     */
    default Component formatFooter(String input) {
        return formatTitle(input);
    }

    /**
     * Get a prefix, which is put before most command messages.
     *
     * @return a preformatted colour sequence
     */
    Component getPrefix();
}
