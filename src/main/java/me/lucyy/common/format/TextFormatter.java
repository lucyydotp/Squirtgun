package me.lucyy.common.format;

import me.lucyy.common.command.FormatProvider;
import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;
import java.util.List;

/**
 * Formats text, supporting gradients and hex codes.
 * This was very heavily inspired by IridiumColorAPI
 * https://github.com/Iridium-Development/IridiumColorAPI
 */
public class TextFormatter {

    private static final List<FormatPattern> patterns = Arrays.asList(new RgbGradientPattern(),
            new HsvGradientPattern(), new HexPattern());

    /**
     * Repeats a character to create a string.
     * @param charToRepeat the character to repeat
     * @param count the amount of times to repeat it
     */
    public static String repeat(String charToRepeat, int count) {
        StringBuilder builder = new StringBuilder();
        for (int x = 0; x < count; x++) builder.append(charToRepeat);
        return builder.toString();
    }

    private static final String TITLE_SEPARATOR = "᠄"; // U+1804 Mongolian Colon - works perfectly for MC strikethrough
    private static final int CHAT_WIDTH = 315; // leave some padding just in case

    /**
     * Creates a centre-aligned title bar for use in commands. Input is formatted using the main colour, the bars either
     * side are formatted using the accent colour.
     * @param in the text to use as a title
     * @param format the format to apply to the string
     * @return a formatted string, ready to send to the player
     */
    public static String formatTitle(String in, FormatProvider format) {
        int spaceLength = (CHAT_WIDTH - TextWidthFinder.findWidth(in) - 6) / 2;
        String line = format.formatAccent(repeat(TITLE_SEPARATOR, spaceLength / 5), "m");
        return line + format.formatMain(" " + in) + " " + line;
    }

    /**
     * Parse a string colour representation to a {@link ChatColor}.
     *
     * @param in a string representation, either as:
     *           <ul>
     *           <li>a single character as in a standard formatter code</li>
     *           <li>a 6-digit HTML hex code, prepended with # ie #ff00ff</li>
     *           </ul>
     * @return the ChatColor representation, or null if it could not be parsed
     */
    public static ChatColor colourFromText(String in) {
        if (in.length() == 1) return ChatColor.getByChar(in.charAt(0));
        else if (in.length() == 7 && in.startsWith("#")) {
            try {
                return ChatColor.of(in.toLowerCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Parses a string to a set of coloured components, calculating gradients.
     *
     * @param input the string to parse. The following formats are supported:
     *              <ul>
     *              <li>{@literal {#FFFFFF}}text - formatted as a static hex code, similar to vanilla codes</li>
     *              <li>{@literal {#FFFFFF>}text{#000000<}} - RGB gradient between the two specified hex codes</li>
     *              <li>{@literal {hsv:FF0000>}text{FF<}} - HSV gradient. The hex value in the start tag is not an HTML
     *              code - the first two characters are hue, next two are saturation, last two are value. In the closing
     *              tag the value is the finishing hue. All values are in the range 0-255 inclusive.</li>
     *              </ul>
     *              Gradient formats support extra format tags, as a list of vanilla characters following a colon. For
     *              example, a gradient from #FFFFFF, in bold and italic, would start {@literal {#FFFFFF:lo}>}.
     * @return the formatted text
     */
    public static String format(String input) {
        return format(input, null);
    }

    /**
     * Parses a string to a set of coloured components, calculating gradients.
     *
     * @param input as for {@link #format(String)}
     * @param overrides a string of vanilla formatters to add to the text
     * @return the formatted text
     */
    public static String format(String input, String overrides) {
        String output = input;
        for (FormatPattern pattern : patterns) {
            output = pattern.process(output, overrides);
        }
        return ChatColor.translateAlternateColorCodes('&', output);
    }

    /**
     * Create an array of evenly-spaced integers between two values. Values are calculated as floats and are then
     * rounded.
     *
     * @param count the length of the returned array
     * @param val1  the value to start from
     * @param val2  the value to end at
     * @return an integer array of length count
     */
    public static int[] fade(int count, int val1, int val2) {
        float step = (val2 - val1) / (float) (count - 1);

        int[] output = new int[count];

        for (int x = 0; x < count; x++) {
            float result = (val1) + step * x;
            while (result < 0) result += 360;
            while (result > 360) result -= 360;
            output[x] = Math.round(result);
        }
        return output;
    }
}