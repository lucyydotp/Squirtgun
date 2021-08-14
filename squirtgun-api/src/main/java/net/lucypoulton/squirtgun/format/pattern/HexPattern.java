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

package net.lucypoulton.squirtgun.format.pattern;

import net.lucypoulton.squirtgun.format.TextFormatter;
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
