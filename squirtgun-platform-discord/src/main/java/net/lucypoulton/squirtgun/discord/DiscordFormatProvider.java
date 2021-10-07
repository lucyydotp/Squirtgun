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
package net.lucypoulton.squirtgun.discord;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.lucypoulton.squirtgun.format.FormatProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum DiscordFormatProvider implements FormatProvider {
    INSTANCE;

    private Map<TextDecoration, TextDecoration.State> asMap(@NotNull TextDecoration[] formatters) {
        return Arrays.stream(formatters).collect(Collectors.toMap(e -> e, e -> TextDecoration.State.TRUE));
    }

    @Override
    public Component formatMain(@NotNull String input, @NotNull TextDecoration[] formatters) {
        return Component.text(input).decorations(asMap(formatters));
    }

    @Override
    public Component formatAccent(@NotNull String input, @NotNull TextDecoration[] formatters) {
        return Component.text(input).decorations(asMap(formatters)).decorate(TextDecoration.BOLD);
    }

    @Override
    public Component formatTitle(String input) {
        return Component.text(input).decorate(TextDecoration.BOLD).decorate(TextDecoration.UNDERLINED);
    }

    @Override
    public Component formatFooter(String input) {
        return Component.empty();
    }


    @Override
    public Component getPrefix() {
        return Component.empty();
    }
}
