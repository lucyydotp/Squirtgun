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

package me.lucyy.squirtgun.format;

import me.lucyy.squirtgun.format.blocked.BlockedGradient;
import me.lucyy.squirtgun.format.blocked.BlockedGradientPattern;
import me.lucyy.squirtgun.format.pattern.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyFormat;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formats text, supporting gradients and hex codes.
 * This was inspired by IridiumColorAPI and CMI
 * https://github.com/Iridium-Development/IridiumColorAPI
 */
@SuppressWarnings("unused")
public class TextFormatter {

    private static final List<FormatPattern> patterns = new ArrayList<>();
    private static final Pattern formatterPattern = Pattern.compile("\\{[^{}]+>}.*\\{[^{}]+<}|(\\{[^{}]+})?[^{}]+");
    //private static final String TITLE_SEPARATOR = "᠄"; // U+1804 Mongolian Colon - works perfectly for MC strikethrough
    private static final String TITLE_SEPARATOR = " ";
    private static final int CHAT_WIDTH = 320;

    static {
        patterns.add(new RgbGradientPattern());
        patterns.add(new HsvGradientPattern());
        patterns.add(new HexPattern());
        patterns.add(new LegacyAmpersandPattern());

        patterns.add(new BlockedGradientPattern("flag",
                        new BlockedGradient(
                                new String[]{"transgender", "trans"},
                                TextColor.color(0x55cdfc),
                                TextColor.color(0xf7a8b8),
                                NamedTextColor.WHITE,
                                TextColor.color(0xf7a8b8),
                                TextColor.color(0x55cdfc)
                        ),
                        new BlockedGradient(
                                new String[]{"bisexual", "bi"},
                                TextColor.color(0xd60270),
                                TextColor.color(0xd60270),
                                TextColor.color(0x9b4f96),
                                TextColor.color(0x0038a8),
                                TextColor.color(0x0038a8)
                        ),
                        new BlockedGradient(
                                "lesbian",
                                TextColor.color(0xD62900),
                                TextColor.color(0xFF9B55),
                                NamedTextColor.WHITE,
                                TextColor.color(0xD461A6),
                                TextColor.color(0xA50062)
                        ),
                        new BlockedGradient(
                                new String[]{"nonbinary", "non-binary", "enby"},
                                TextColor.color(0xfff430),
                                NamedTextColor.WHITE,
                                TextColor.color(0x9c59d1),
                                NamedTextColor.BLACK
                        ),
                        new BlockedGradient(
                                new String[]{"pansexual", "pan"},
                                TextColor.color(0xff1b8d),
                                TextColor.color(0xffda00),
                                TextColor.color(0x1bb3ff)

                        ),
                        new BlockedGradient(
                                new String[]{"asexual", "ace"},
                                NamedTextColor.BLACK,
                                TextColor.color(0xa3a3a3),
                                NamedTextColor.WHITE,
                                TextColor.color(0x810082)
                        ),
                        new BlockedGradient(
                                "genderqueer",
                                TextColor.color(0xb77fdd),
                                NamedTextColor.WHITE,
                                TextColor.color(0x48821e)
                        ),
                        new BlockedGradient(
                                new String[]{"genderfluid", "fluid"},
                                TextColor.color(0xff76a3),
                                NamedTextColor.WHITE,
                                TextColor.color(0xbf11d7),
                                NamedTextColor.BLACK,
                                TextColor.color(0x303cbe)
                        )
                )
        );
    }

    /**
     * Repeats a character to create a string.
     *
     * @param charToRepeat the character to repeat
     * @param count        the amount of times to repeat it
     */
    @Contract(pure = true)
    public static String repeat(@NotNull final String charToRepeat, final int count) {
        final StringBuilder builder = new StringBuilder();
        for (int x = 0; x < count; x++) builder.append(charToRepeat);
        return builder.toString();
    }

    /**
     * Inverts a component, flipping the extra components and adding the original component on the end.
     */
    @Contract(pure = true)
    public static Component invert(final Component component) {
        final List<Component> comps = new ArrayList<>();
        for (int i = component.children().size() - 1; i >= 0; i--) {
            comps.add(component.children().get(i));
        }
        comps.add(component.children(new ArrayList<>()));

        return comps.get(0).children(comps.subList(1, comps.size()));
    }

    /**
     * Creates a centre-aligned title bar for use in commands. Input is formatted using the main colour, the bars either
     * side are formatted using the accent colour.
     *
     * @param in     the text to use as a title
     * @param format the format to apply to the string
     * @return a formatted string, ready to send to the player
     */
    @Contract(pure = true)
    public static Component formatTitle(@NotNull final String in, @NotNull final FormatProvider format) {
        return centreText(in, format, TITLE_SEPARATOR, new TextDecoration[]{TextDecoration.STRIKETHROUGH});
    }

    /**
     * Inserts characters either side of an input string to centre-align it in chat.
     *
     * @param in        the string to centre
     * @param format    what to format the input with. Centred text is main-formatted, edges are accent-formatted
     * @param character the character to repeat to centre-align the text
     * @return a formatted string containing the centred text
     */
    @Contract(pure = true)
    public static Component centreText(@NotNull final String in, @NotNull final FormatProvider format,
                                       @NotNull String character) {
        return centreText(in, format, character, new TextDecoration[0]);
    }

    /**
     * Inserts characters either side of an input string to centre-align it in chat.
     *
     * @param in         the string to centre
     * @param format     what to format the input with. Centred text is main-formatted, edges are accent-fornatted
     * @param character  the character to repeat to centre-align the text
     * @param formatters a list of vanilla formatter codes to apply to the edges
     * @return a formatted string containing the centred text
     */
    @Contract(pure = true)
    public static Component centreText(@NotNull final String in, @NotNull final FormatProvider format,
                                       @NotNull final String character, @NotNull final TextDecoration[] formatters) {
        final int spaceLength = (CHAT_WIDTH - TextWidthFinder.findWidth(in) - 6) / 2; // take off 6 for two spaces
        final Component line = format.formatAccent(
                repeat(character, spaceLength / TextWidthFinder.findWidth(character)),
                formatters);

        Component centre = format.formatMain(" " + in + " ");
        for (TextDecoration deco : formatters) centre = centre.decoration(deco, false);
        return line.append(centre).append(invert(line));
    }

    /**
     * Parse a string colour representation to a {@link TextColor}.
     *
     * @param in a string representation, either as:
     *           <ul>
     *           <li>a single character as in a standard formatter code</li>
     *           <li>a 6-digit HTML hex code, prepended with # ie #ff00ff</li>
     *           </ul>
     * @return the ChatColor representation, or null if it could not be parsed
     */
    @Contract(pure = true)
    public static @Nullable
    TextColor colourFromText(@NotNull final String in) {
        if (in.length() == 1) return Objects.requireNonNull(LegacyComponentSerializer.parseChar(in.charAt(0))).color();
        else if (in.length() == 7 && in.startsWith("#")) {
            try {
                return TextColor.fromCSSHexString(in);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Parses a string to a set of coloured components, calculating gradients.
     * By default, any section-coded legacy colours in the string will be removed. Ampersand-coded colours will be
     * parsed into components. This behaviour can be changed with {@link #format(String, String, boolean)}
     *
     * @param input the string to parse. encoded with a sort of markup language:
     *              <ul>
     *              <li>{@literal &c}text - vanilla format codes, using &amp; as per bukkit conventions</li>
     *              <li>{@literal {#FFFFFF}}text - static HTML code, similar to vanilla codes</li>
     *              <li>{@literal {#FFFFFF>}text{#000000<}} - RGB gradient between the two specified HTML codes</li>
     *              <li>{@literal {hsv:FF0000>}text{FF<}} - HSV gradient. The hex value in the start tag is not an HTML
     *              code - the first two characters are hue, next two are saturation, last two are value. In the closing
     *              tag the value is the finishing hue. All values are in the range 0-255 (00-FF) inclusive.</li>
     *              <li>{@literal {flag:some-kind-of-flag>}text{flag<}} - blocked flags. Still WIP, more info to come
     *              Soon&trade;. Currently supports miscellaneous common pride flags.</li>
     *              </ul>
     *              <p>
     *              Gradient formats support extra format tags, as a list of vanilla characters following a colon. For
     *              example, a gradient from #FFFFFF, in bold and italic, would start {@literal {#FFFFFF:lo}>}.
     *              <p>
     *              Tags should not be nested - doing so will result in undefined behaviour.
     * @return a component containing the parsed code.
     */
    @Contract(pure = true)
    public static Component format(@NotNull final String input) {
        return format(input, null, false);
    }

    /**
     * Parses a string to a set of coloured components, calculating gradients.
     *
     * @param input                   as for {@link #format(String)}
     * @param overrides               a string of vanilla formatters to add to the text
     * @param usePredefinedFormatters whether to use §-prefixed codes. if true then they will take priority over LCL
     *                                formatters, if false then they will be removed prior to formatting
     * @return the formatted text
     */
    @Contract(pure = true)
    public static Component format(@NotNull String input, @Nullable final String overrides,
                                   final boolean usePredefinedFormatters) {
        if ((usePredefinedFormatters && input.contains("\u00a7"))) {
            return LegacyComponentSerializer.legacySection().deserialize(input.replaceAll("(?<!\\\\)(\\{[^}]*}|&[0-9A-Fa-fK-Ok-o])",
                    ""));
        }
        input = input.replaceAll("\u00a7.", "");
        Component output = null;
        Matcher matcher = formatterPattern.matcher(input);
        while (matcher.find()) {
            boolean hasBeenParsed = false;
            for (FormatPattern pattern : patterns) {
                Component component = pattern.process(matcher.group(), overrides);
                if (component != null) {
                    hasBeenParsed = true;
                    if (output == null) output = component;
                    else output = output.append(component);
                    break;
                }
            }
            if (!hasBeenParsed) {
                Component component = Component.text(matcher.group());
                if (output == null) output = component;
                else output = output.append(component);
            }
        }
        return output;
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
    @Contract(pure = true)
    public static int[] fade(final int count, final int val1, final int val2) {
        final float step = (val2 - val1) / (float) (count - 1);

        final int[] output = new int[count];

        for (int x = 0; x < count; x++) {
            float result = (val1) + step * x;
            while (result < 0) result += 360;
            while (result > 360) result -= 360;
            output[x] = Math.round(result);
        }
        return output;
    }

    /**
     * Apply a string of legacy decoration codes to a component.
     *
     * @param in          the component to apply decorations to
     * @param decorations a list of legacy decorations to apply ie "lo" will apply bold and italic
     * @throws IllegalArgumentException if a decoration provided is invalid
     */
    @Contract(pure = true)
    public static Component applyLegacyDecorations(@NotNull final Component in, @Nullable final String decorations) {
        if (decorations == null) return in;
        Component out = in;
        for (char c : decorations.toCharArray()) {
            final LegacyFormat format = LegacyComponentSerializer.parseChar(c);
            if (format == null) throw new IllegalArgumentException("Invalid char " + c);
            final TextDecoration deco = format.decoration();
            if (deco == null) throw new IllegalArgumentException("Char " + c + " is not a decorator");
            out = out.decorate(deco);
        }
        return out;
    }
}