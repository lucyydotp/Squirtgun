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
package net.lucypoulton.squirtgun.discord.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serialises Components into Discord-formatted markdown. Only {@link TextComponent}s
 * are supported. Discord's text format is far less rich than Minecraft's, so some
 * information, such as colour and events, is discarded.
 */
public enum DiscordComponentSerializer implements ComponentSerializer<Component, TextComponent, String> {

    INSTANCE;

    private static final Map<TextDecoration, String> DECORATION_MARKUP = Map.of(
        TextDecoration.BOLD, "**",
        TextDecoration.ITALIC, "*",
        TextDecoration.UNDERLINED, "__",
        TextDecoration.STRIKETHROUGH, "~~",
        TextDecoration.OBFUSCATED, "||"
    );

    @Override
    public @NotNull TextComponent deserialize(@NotNull String input) {
        return Component.text(input);
    }

    @Override
    public @NotNull String serialize(@NotNull Component component) {

        if (!(component instanceof TextComponent)) return component.toString();

        TextComponent text = (TextComponent) component;

        String formatters = text.decorations().entrySet().stream()
            .filter(entry -> entry.getValue().equals(TextDecoration.State.TRUE))
            .map(entry -> DECORATION_MARKUP.get(entry.getKey()))
            .collect(Collectors.joining());

        StringBuilder output = new StringBuilder();

        output.append(formatters);
        output.append(text.content().replaceAll("(?<sym>[*_~|])", "\\\\${sym}"));
        output.append(new StringBuilder(formatters).reverse());

        for (Component next : text.children()) {
            output.append(serialize(next));
        }
        return output.toString();
    }
}
