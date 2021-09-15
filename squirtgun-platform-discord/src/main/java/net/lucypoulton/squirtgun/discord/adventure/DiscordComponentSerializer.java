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
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.format.TextDecoration.State;

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

    private static boolean shouldAdd(State thisState, State otherState) {
        if (thisState == State.NOT_SET) {
            return otherState == State.TRUE;
        }
        return thisState == State.TRUE;
    }


    @Override
    public @NotNull TextComponent deserialize(@NotNull String input) {
        return Component.text(input);
    }

    private @NotNull String serialize(@NotNull Component component, @Nullable Style parentStyle) {

        Style inherited = parentStyle == null ? Style.empty() : parentStyle;

        String formatters = component.decorations().entrySet().stream()
            // don't add the decoration if it's already present in the parent
            .filter(entry -> shouldAdd(entry.getValue(), inherited.decoration(entry.getKey())))
            .map(entry -> DECORATION_MARKUP.get(entry.getKey()))
            .collect(Collectors.joining());

        String content = (component instanceof TextComponent) ?
            ((TextComponent) component).content() :
            PlainTextComponentSerializer.plainText().serialize(component);

        StringBuilder output = new StringBuilder();

        output.append(formatters);
        output.append(content.replaceAll("(?<sym>[*_~|])", "\\\\${sym}"));
        output.append(new StringBuilder(formatters).reverse());

        Style merged = component.style().merge(inherited, Style.Merge.Strategy.ALWAYS);

        for (Component next : component.children()) {
            output.append(serialize(next, merged));
        }

        output.append(new StringBuilder(formatters).reverse());

        // tidy up output
        return output.toString().replaceAll("([*_~|])\\1{3}", "");
    }

    @Override
    public @NotNull String serialize(@NotNull Component component) {
        return serialize(component, null);
    }
}
