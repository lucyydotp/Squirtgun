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
import net.lucypoulton.squirtgun.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.format.TextDecoration.State;

/**
 * Serialises Components into Discord-formatted markdown. Discord's text format is
 * far less rich than Minecraft's, so some information, such as colour and events,
 * is discarded.
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
        Set<TextDecoration> decorations = new HashSet<>();
        StringBuilder textBuffer = new StringBuilder();

        List<TextComponent> out = new ArrayList<>();

        List<Character> charList = new ArrayList<>(2);
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            switch (c) {
                case '*':
                case '_':
                case '~':
                case '|':
                    charList.add(c);
                    continue;
                default:
                    if (charList.size() != 0) {
                        String decoString = charList.stream().map(String::valueOf).collect(Collectors.joining());
                        TextDecoration decoration = DECORATION_MARKUP.entrySet().stream()
                                .filter(set -> set.getValue().equals(decoString))
                                .map(Map.Entry::getKey)
                                .findFirst().orElse(null);

                        charList.clear();

                        if (textBuffer.length() != 0) {
                            out.add((TextComponent) Component.text(textBuffer.toString()).decorations(
                                    decorations.stream().collect(Collectors.toMap(deco -> deco, x -> State.TRUE))
                            ));
                        }

                        textBuffer = new StringBuilder();

                        if (decorations.contains(decoration)) {
                            decorations.remove(decoration);
                        } else {
                            decorations.add(decoration);
                        }
                    }
            }
            textBuffer.append(c);
        }

        if (textBuffer.length() != 0) {
            out.add((TextComponent) Component.text(textBuffer.toString()).decorations(
                    decorations.stream().collect(Collectors.toMap(deco -> deco, x -> State.TRUE))
            ));
        }

        return out.size() > 1 ? Component.empty().children(out) : out.get(0);
    }


    private String getStringValue(Component in) {
        if (in instanceof TextComponent) return ((TextComponent) in).content();
        return PlainTextComponentSerializer.plainText().serialize(in);
    }

    private Set<TextDecoration> styleToDecorationSet(Style style) {
        return style.decorations().entrySet().stream()
            .filter(deco -> deco.getValue() == State.TRUE)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    private List<Pair<String, Set<TextDecoration>>> flatten(Component component, @Nullable Component parent) {

        if (parent == null) {
            parent = Component.empty();
        }

        Component merged = parent.mergeStyle(component);
        List<Pair<String, Set<TextDecoration>>> out = new ArrayList<>();
        out.add(new Pair<>(getStringValue(component), styleToDecorationSet(merged.style())));

        for (Component child : component.children()) {
            out.addAll(flatten(child, parent.mergeStyle(component)));
        }
        return out;
    }

    @Override
    public @NotNull String serialize(@NotNull Component component) {
        // flatten
        List<Pair<String, Set<TextDecoration>>> flattened = flatten(component, null);

        Stack<TextDecoration> currentDecorations = new Stack<>();
        StringBuilder output = new StringBuilder();

        for (Pair<String, Set<TextDecoration>> entry : flattened) {

            // finish all decorations that aren't applicable
            while (!currentDecorations.isEmpty() && !entry.value().contains(currentDecorations.peek())) {
                TextDecoration deco = currentDecorations.pop();
                output.append(DECORATION_MARKUP.get(deco));
            }

            // add any missing decorations
            entry.value().stream().filter(deco -> !currentDecorations.contains(deco)).forEach(deco -> {
                currentDecorations.push(deco);
                output.append(DECORATION_MARKUP.get(deco));
            });

            // add the text
            output.append(entry.key());
        }

        // finish any outstanding decorations
        while (!currentDecorations.isEmpty()) {
            output.append(DECORATION_MARKUP.get(currentDecorations.pop()));
        }
        return output.toString();
    }
}
